### Exception when starting Jar 
Exception in thread "main" java.lang.NoSuchMethodException: net.kotlincook.voting.VotingApplication.main([Ljava.lang.String;)
Solution: Define the main class in the pom (with the suffix Kt):
```
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <mainClass>net.kotlincook.voting.VotingApplicationKt</mainClass>
    </configuration>
```