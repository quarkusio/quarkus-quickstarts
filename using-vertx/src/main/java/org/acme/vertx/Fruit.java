/*
 * Copyright 2019 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.acme.vertx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import io.reactiverse.axle.pgclient.*;

public class Fruit {

    public Long id;

    public String name;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }

    public Fruit(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CompletionStage<List<Fruit>> findAll(PgPool client) {
        return client.query("SELECT id, name FROM fruits ORDER BY name ASC").thenApply(pgRowSet -> {
            List<Fruit> list = new ArrayList<>(pgRowSet.size());
            for (Row row : pgRowSet) {
                list.add(from(row));
            }
            return list;
        });
    }

    public static CompletionStage<Fruit> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT id, name FROM fruits WHERE id = $1", Tuple.of(id))
                .thenApply(PgRowSet::iterator)
                .thenApply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public CompletionStage<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO fruits (name) VALUES ($1) RETURNING (id)", Tuple.of(name))
                .thenApply(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public CompletionStage<Boolean> update(PgPool client) {
        return client.preparedQuery("UPDATE fruits SET name = $1 WHERE id = $2", Tuple.of(name, id))
                .thenApply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static CompletionStage<Boolean> delete(PgPool client, Long id) {
        return client.preparedQuery("DELETE FROM fruits WHERE id = $1", Tuple.of(id))
                .thenApply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static Fruit from(Row row) {
        return new Fruit(row.getLong("id"), row.getString("name"));
    }
}
