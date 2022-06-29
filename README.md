# xyzla-java-plugins

```
902  mvn versions:set -DnewVersion=1.1.2 -DallowSnopShot=true
903  mvn versions:commit

922  mvn clean deploy -projects xyzla-common,xyzla-spring
923  mvn clean deploy -projects xyzla-common,xyzla-spring -P release
```


```
mvn clean deploy -projects xyzla-spring-autoconfig,xyzla-spring-starter -P releas
 
mvn clean deploy -projects xyzla-common -P release
```