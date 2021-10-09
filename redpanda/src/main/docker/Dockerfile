FROM docker.vectorized.io/vectorized/redpanda:v21.9.4

# Redpanda schema registry, not exposed by base image
EXPOSE 8081

CMD [ "redpanda", \
        "start", \
        "--smp", \
        "1", \
        "--reserve-memory", \
        "0M", \
        "--overprovisioned", \
        "--node-id", \
        "0", \
        "--check=false", \
        "--kafka-addr", \
        "PLAINTEXT://0.0.0.0:29092,OUTSIDE://0.0.0.0:9092", \
        "--advertise-kafka-addr", \
        "PLAINTEXT://redpanda:29092,OUTSIDE://redpanda:9092" \
        ]
