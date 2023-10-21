## g1gc tunning
kubectl set env deployment/eager-factorial JDK_JAVA_OPTIONS="-XX:MaxGCPauseMillis=150 -Xlog:gc*=debug:file=/tmp/gc.log"

## parallel gc
kubectl set env deployment/eager-factorial JDK_JAVA_OPTIONS="-XX:+UseParallelGC -Xlog:gc*=debug:file=/tmp/gc.log"
kubectl set env deployment/eager-factorial JDK_JAVA_OPTIONS="-XX:+UseParallelGC -XX:MaxGCPauseMillis=200 -Xlog:gc*=debug:file=/tmp/gc.log"

## gen zgc
kubectl set env deployment/eager-factorial JDK_JAVA_OPTIONS="-XX:+UseZGC -XX:+ZGenerational -Xlog:gc*=debug:file=/tmp/gc.log"
kubectl set env deployment/eager-factorial JDK_JAVA_OPTIONS="-XX:+UseZGC -XX:+ZGenerational -Xmx1024m -Xlog:gc*=debug:file=/tmp/gc.log"

