## Directory Structure

My proposal for directory structure is the following:

- core – all the core classes used by the app
    - common – all common functionality
        - usecase – globally available use cases
	- di – Dependency Injection
	- etc – Everything else that cannot fit into a category
	- ..
- feature – the specific features that the app provides like: 
    - elements, pictures, organizer, etc.
	- ..


## Base Classes 

Base classes provide automatic layout binding and access to scoped services such as loading, saving or deleting an image.

How to inherit? See the classes documentation in the source code.

Note: Currently not all classes are properly inheriting their base classes but I hope that we can change that. 

## Use Cases

A use case is an intention, in other words, something we want to do in our application. Its main responsibility is to orchestrate our domain logic and its connection with both UI and Data layers.

Use cases are invoked with an IODispatcher and the result comes by convention on the Main UI thread.

To make a use case inherit from the UseCase class and overide `run(..)`. There are two generic parameters that need to be defined when inheriting from the UseCase class: ResultType, Params.

- Result Type - what will actually come out of the function. 
- Params - parameters that will be used within the use case function

the actual return type of `run(..)` is `Either<ResultType, Failure> `. This way if a use case fails for some reason we can handle it gracefully without using exceptions.


## Dependencies and Dagger

Just a quick info here.

If you need to create a use case or any other object to be used globally, use the `AppModule` (the place where you create your objects) to create and expose it to the `AppComponent` (the dependency graph - the actual place where the objects stay). Example:

```
class AppModule(val application: BaseApplication) {
    @Provides // Dagger cannot instantiate classes without @Provides

    @AppScope // The @Scope annotation just tells Dagger
              // instantiate only one time the class within a given scope.
    
    // declaring a function like this tells Dagger how to create the object
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteFileFromInternalStorage =
        DeleteFileFromInternalStorage()
}

@Component(modules = [AppModule::class]) // Tells dagger which is the module that creates the graph
@AppScope
interface AppComponent {
    // expose the class into the graph
    fun deleteBitmapsFromInternalStorage(): DeleteFileFromInternalStorage
}

```

To add the objects from the graph into an activity and/or fragment just use their respective graph instances. For example this is how you would setup the instance for the deleteFilesFromInternalStorage use case to be available globally:

```

class BaseApplication : Application() {
    // instantiate the graph
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    private lateinit var deleteFilesFromInternalStorage : DeleteFilesFromInternalStorage
    override fun onCreate() {
        // get the object from the graph
        deleteFilesFromInternalStorage = appComponent.deleteFilesFromInternalStorage()
        super.onCreate()
    }
}
```