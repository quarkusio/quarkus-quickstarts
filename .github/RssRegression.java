//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.ibm.cos:ibm-cos-java-sdk:2.1.0
//DEPS javax.xml.bind:jaxb-api:2.4.0-b180830.0359

package io.quarkus;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.AmazonS3Exception;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.System.exit;

public class RssRegression {

    private static AmazonS3 _cosClient;
    private static String ENDPOINT_URL;
    private static String LOCATION;

    static {
        ENDPOINT_URL = "https://s3.eu-gb.cloud-object-storage.appdomain.cloud";
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
        LOCATION = "eu-gb";
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        if(args.length != 6){
            System.out.println(args.length);
            printUsage();
            exit(1);
        }
        String BUCKET_NAME = args[0];
        String API_KEY = args[1];
        String SERVICE_INSTANCE_ID = args[2];
        String bucketFile = args[3];
        String oldPmapFile = args[4];
        String newPmapFile = args[5];

        _cosClient = createClient(API_KEY, SERVICE_INSTANCE_ID, ENDPOINT_URL, LOCATION);

        System.out.println("Downloading file from storage: " + bucketFile + " to local: " + oldPmapFile);
        try{
            downloadFile(_cosClient, BUCKET_NAME, bucketFile, oldPmapFile);
        } catch (AmazonS3Exception s3Execpetion){
            System.out.println("Download failed with: " + s3Execpetion.getMessage());
            _cosClient.shutdown();
            exit(1);
        }

        int oldRss = getRssFromFile(oldPmapFile);
        int newRss = getRssFromFile(newPmapFile);
        boolean rssRegression = false;

        if( oldRss != -1 && newRss != -1) {
            System.out.println("Old RSS: " + oldRss);
            System.out.println("New RSS: " + newRss);

            rssRegression = newRss > (oldRss * 1.1);
            System.out.println("Regression: " + rssRegression);

            System.out.println("Uploading new pmap file to storage: " + bucketFile + " from local: " + oldPmapFile);
            uploadObject(_cosClient, BUCKET_NAME, bucketFile, newPmapFile);
        }

        _cosClient.shutdown();

        if(rssRegression) {
            exit(1);
        }
        else {
            exit(0);
        }
    }

    private static void printUsage() {
        System.out.println("Usage: jbang RssRegression BUCKET_NAME API_KEY SERVICE_INSTANCE_ID REMOTE_OBJECT_NAME OLD_PMAP_FILENAME NEW_PMAP_FILENAME");
        System.out.println("");
        System.out.println("   Where;");
        System.out.println("      BUCKET_NAME - name of S3 bucket to contain pmap object");
        System.out.println("      API_KEY - Private API key");
        System.out.println("      SERVICE_INSTANCE_ID - Cloud Resource Name (CRN) id of S3 bucket");
        System.out.println("      REMOTE_OBJECT_NAME - name of S3 object stored in S3 bucket");
        System.out.println("      OLD_PMAP_FILENAME - local filename of to download previous run pmap file");
        System.out.println("      NEW_PMAP_FILENAME - name of filename for current run pmap file");
    }

    /**
     * @param api_key
     * @param service_instance_id
     * @param endpoint_url
     * @param location
     * @return AmazonS3
     */
    public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String
            location) {
        AWSCredentials credentials;
        credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig).build();
        return cosClient;
    }

    /**
     * @param cosClient
     * @param BUCKET_NAME
     * @param bucketFile
     * @param inputFile
     */
    public static void uploadObject(AmazonS3 cosClient, String BUCKET_NAME, String bucketFile, String inputFile){
        cosClient.putObject(
                BUCKET_NAME, // the name of the destination bucket
                bucketFile, // the object key
                new File(inputFile) // the file name and path of the object to be uploaded
        );
    }

    /**
     * @param cosClient
     * @param BUCKET_NAME
     * @param bucketFile
     * @param outputFile
     */    public static void downloadFile(AmazonS3 cosClient, String BUCKET_NAME, String bucketFile, String outputFile) throws AmazonS3Exception {
        GetObjectRequest request = new // create a new request to get an object
                GetObjectRequest( // request the new object by identifying
                BUCKET_NAME, // the name of the bucket
                bucketFile // the name of the object
        );

        cosClient.getObject( // write the contents of the object
                request, // using the request that was just created
                new File(outputFile) // to write to a new file
        );
    }

    public static int getRssFromFile(String filename){
        int rss = -1;
        try {
            String lastLine = readLastLine(filename);
            if (!lastLine.equals("")){
                while(lastLine.length() != (lastLine = lastLine.replaceAll("  ", " ")).length()){}
                rss = Integer.parseInt(lastLine.split(" ")[3]);
            } else {
                System.out.println("File was empty: " + filename);;
            }
        } catch (IOException e) {
            System.out.println("Read file failed with: " + e.getMessage());
        }
        return rss;

    }

    public static String readLastLine(String filename) throws IOException {
        String sCurrentLine, lastLine = "";

        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            while ((sCurrentLine = br.readLine()) != null) {
                lastLine = sCurrentLine;
            }
        }

        return lastLine;
    }
}
