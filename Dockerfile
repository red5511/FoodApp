FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine

WORKDIR /app
COPY --from=build /build/target/foodapp-*.jar /app/

# Copy the entrypoint script into the image
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080

# Define environment variables
ENV JAR_VERSION="1.0.0"
#ENV DB_URL="jdbc:postgresql://postgres:5432/food_app"

# Use the custom entrypoint that sets the DB password from the secret
ENTRYPOINT ["/docker-entrypoint.sh"]
