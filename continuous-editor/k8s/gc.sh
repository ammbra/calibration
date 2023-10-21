## g1gc tunning
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:MaxGCPauseMillis=250 -Xmx2816m -Xmx2816m -XX:ParallelGCThreads=4 -XX:ConcGCThreads=1 -Xlog:gc*=debug:file=/tmp/gc.log"

## parallel gc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseParallelGC -Xlog:gc*=debug:file=/tmp/gc.log"
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseParallelGC -XX:MaxGCPauseMillis=200 -XX:GCTimeRatio=99 -Xlog:gc*=debug:file=/tmp/gc.log"

## gen zgc
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseZGC -XX:+ZGenerational -Xlog:gc*=debug:file=/tmp/gc.log"
kubectl set env deployment/continuous-editor JDK_JAVA_OPTIONS="-XX:+UseZGC -XX:+ZGenerational -Xmx2816m -Xlog:gc*=debug:file=/tmp/gc.log"

