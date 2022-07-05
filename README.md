# xyzla-java-plugins

```
mvn versions:set -DnewVersion=1.1.2 -DallowSnopShot=true
mvn versions:commit

mvn clean deploy -projects xyzla-common,xyzla-spring
mvn clean deploy -projects xyzla-common,xyzla-spring -P release
```


```
mvn clean deploy -projects xyzla-spring-autoconfig,xyzla-spring-starter -P releas
 
mvn clean deploy -projects xyzla-common -P release
```