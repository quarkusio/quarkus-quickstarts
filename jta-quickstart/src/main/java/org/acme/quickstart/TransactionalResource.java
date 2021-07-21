package org.acme.quickstart;

import com.arjuna.ats.jta.TransactionManager;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.context.spi.ContextManagerProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/jta")
public class TransactionalResource {
    private ExecutorService excecutorService;

    @PostConstruct
    void postConstruct() {
        excecutorService = Executors.newFixedThreadPool(1);
    }

    private int getTransactionStatus() throws SystemException {
        Transaction transaction = TransactionManager.transactionManager().getTransaction();

        return transaction == null ? Status.STATUS_NO_TRANSACTION : transaction.getStatus();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int noTxn() throws SystemException {
        return getTransactionStatus(); // should return Status.STATUS_NO_TRANSACTION
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    @Path("cmt")
    public int cmt() throws SystemException {
        // the @Transactional annotation will have started a transaction
        return getTransactionStatus(); // should return Status.STATUS_ACTIVE
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("bmt")
    public int bmt() throws SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException, NotSupportedException {
        int status = getTransactionStatus();

        // since there is no @Transactional annotation there should be no active transaction
        if (status != Status.STATUS_NO_TRANSACTION) {
            return status;
        }

        // verify that it is possible to begin and commit a transaction without exceptions
        TransactionManager.transactionManager().begin();
        TransactionManager.transactionManager().commit();

        return getTransactionStatus(); // should be Status.STATUS_NO_TRANSACTION
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    @Path("async-with-suspended")
    public void async1(@Suspended AsyncResponse ar) throws SystemException {
        // the framework will have started a transaction because of the @Transactional annotation
        Transaction txnToPropagate = TransactionManager.transactionManager().getTransaction();
        ContextManagerProvider cmp = ContextManagerProvider.INSTANCE.get();
        ManagedExecutor me = cmp.getContextManager().newManagedExecutorBuilder()
                .propagated(ThreadContext.TRANSACTION)
                .build();

        // the transaction should be active (because of the @Transactional annotation)
        if (getTransactionStatus() != Status.STATUS_ACTIVE) {
            ar.resume(Response.status(Response.Status.PRECONDITION_FAILED)
                            .entity(getTransactionStatus()).build());
        }

        me.submit(() -> {
            try {
                Transaction txn = TransactionManager.transactionManager().getTransaction();

                if (!txn.equals(txnToPropagate)) {
                    // the original transaction, txnToPropagate, should have been propagated to the new thread
                    ar.resume(Response.status(Response.Status.PRECONDITION_FAILED)
                            .entity(getTransactionStatus()).build());
                }

                ar.resume(Response.ok().entity(getTransactionStatus()).build()); // should return Status.STATUS_ACTIVE
            } catch (SystemException e) {
                ar.resume(e);
            }
        });
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    @Path("async-with-completion-stage")
    public CompletionStage<Integer> async2() throws SystemException {
        System.out.printf("submitting async job ...%n");

        ContextManagerProvider cmp = ContextManagerProvider.INSTANCE.get();
        ManagedExecutor me = cmp.getContextManager().newManagedExecutorBuilder()
                .propagated(ThreadContext.TRANSACTION)
                .build();

        Transaction txnToPropagate = TransactionManager.transactionManager().getTransaction();

        return me.supplyAsync(() -> {
            try {
                Transaction txn = TransactionManager.transactionManager().getTransaction();

                if (!txn.equals(txnToPropagate)) {
                    // the original transaction, txnToPropagate, should have been propagated to the new thread
                    return -1;
                }
                return getTransactionStatus(); // should return Status.STATUS_ACTIVE
            } catch (SystemException e) {
                return -1;
            }
        });

    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    @Path("async-6471-reproducer")
    public void asyncIssue6471Reproducer(@Suspended AsyncResponse ar) throws SystemException {
        System.out.printf("submitting async job ...%n");
        Transaction txnToPropagate = TransactionManager.transactionManager().getTransaction();

        // the transaction should be active (because of the @Transactional annotation)
        if (getTransactionStatus() != Status.STATUS_ACTIVE) {
            ar.resume(Response.status(Response.Status.PRECONDITION_FAILED)
                            .entity(getTransactionStatus()).build());
        }

        ContextManagerProvider cmp = ContextManagerProvider.INSTANCE.get();
        ManagedExecutor me = cmp.getContextManager().newManagedExecutorBuilder()
                .propagated(ThreadContext.TRANSACTION)
                .build();

        me.submit(() -> {
            System.out.printf("running async job ...%n");
            try {
                Transaction txn = TransactionManager.transactionManager().getTransaction();

                if (!txn.equals(txnToPropagate)) {
                    // the original transaction, txnToPropagate, should have been propagated to the new thread
                    ar.resume(Response.status(Response.Status.PRECONDITION_FAILED)
                            .entity(getTransactionStatus()).build());
                }

                // execute a long running business activity and resume when done
                System.out.printf("resuming long running async job ...%n");

                // the transaction started via the @Transactional annotation should still be active
                // but due to Quarkus issue 6471 there is no interceptor for extending the transaction bondary
                // (see the issue for further details)
                ar.resume(Response.ok().entity(getTransactionStatus()).build()); // should return Status.STATUS_ACTIVE
            } catch (SystemException e) {
                System.out.printf("resuming async job with exception: %s%n", e.getMessage());

                ar.resume(e);
            }
        });
    }
}
