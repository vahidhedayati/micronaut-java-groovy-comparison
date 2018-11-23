package comparison.java.groovy.client


import comparison.java.groovy.view.Product
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Maybe

@Client(id = "product" , path="/products")
interface ProductClient {


    @Get("/")
    Maybe<List<Product>> list()


    @Get("/find/{name}")
    Maybe<Product> find(String name)



}




/**

 Attempting to add a product client fall back identical to java example in the groovy project causes this uss
 Caused by: BUG! exception in phase 'canonicalization' in source unit '/home/xxx/micro-projects/micronaut-java-groovy-comparison/test-groovy/src/main/groovy/comparison.java.groovy/client/ProductFall.groovy' null
 at org.gradle.api.internal.tasks.compile.ApiGroovyCompiler.execute(ApiGroovyCompiler.java:178)
 at org.gradle.api.internal.tasks.compile.ApiGroovyCompiler.execute(ApiGroovyCompiler.java:56)
 at org.gradle.api.internal.tasks.compile.GroovyCompilerFactory$DaemonSideCompiler.execute(GroovyCompilerFactory.java:74)
 at org.gradle.api.internal.tasks.compile.GroovyCompilerFactory$DaemonSideCompiler.execute(GroovyCompilerFactory.java:62)
 at org.gradle.api.internal.tasks.compile.daemon.AbstractDaemonCompiler$CompilerCallable.call(AbstractDaemonCompiler.java:88)
 at org.gradle.api.internal.tasks.compile.daemon.AbstractDaemonCompiler$CompilerCallable.call(AbstractDaemonCompiler.java:76)
 at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:42)
 at org.gradle.workers.internal.WorkerDaemonServer.execute(WorkerDaemonServer.java:46)
 at org.gradle.workers.internal.WorkerDaemonServer.execute(WorkerDaemonServer.java:30)
 at org.gradle.process.internal.worker.request.WorkerAction.run(WorkerAction.java:101)
 at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:35)
 at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
 at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:155)
 at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:137)
 at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:404)


 **/