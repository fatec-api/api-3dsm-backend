FROM quay.io/keycloak/keycloak:25.0

COPY providers/ /opt/keycloak/providers/

RUN /opt/keycloak/bin/kc.sh build

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]