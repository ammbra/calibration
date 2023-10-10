## g1gc tunning
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:ParallelGCThreads=8 -XX:ConcGCThreads=2 -Xlog:gc*=debug:file=/tmp/gc.log"

## parallel gc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseParallelGC -Xlog:gc*=debug:file=/tmp/gc.log"
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseParallelGC -XX:GCTimeRatio=5 -Xlog:gc*=debug:file=/tmp/gc.log"

## gen zgc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseZGC -XX:+ZGenerational -Xlog:gc*=debug:file=/tmp/gc.log"
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseZGC -XX:+ZGenerational -Xmx2816m -Xlog:gc*=debug:file=/tmp/gc.log"

