Quarkus guide: https://quarkus.io/guides/websockets-next-reference

This quickstart shows how `SecurityIdentity` can be updated using an access token obtained with Keycloak JavaScript adapter.
First, start Quarkus application in DEV mode using `quarkus dev` command, and then navigate to the `http://localhost:8080/` page.
You will be redirected to Keycloak where you can log in using well-known user `alice` or `bob`.
Once the Web socket connection is opened, the Keycloak JavaScript adapter will update current access token when necessary and new token can be propagated to the WebSocket server as message.
