# sliide-tech_task

# Getting Started

### Requirements

https://github.com/sliide/technical_task_android?tab=readme-ov-file

I'm not sure if I understood the requirements correctly:
'(only users from last page of the endpoint)'.
They look a little unusual. So there are 2 methods in com.sliide.data.users.UsersRepo:
```
 suspend fun lastUsers(): Result<List<User>>
 
 suspend fun users(): Result<List<User>>
```

The method lastUsers() is used. But if there is a misunderstanding, please, check another one.

# Build and Test

* Before build the project add accessToken to local.properties file. Like this:

```
 accessToken=f83ad7e6319......
```

[Get your access token](https://gorest.co.in/my-account/access-tokens)

* To build the project, from the commandline, run ./gradlew clean build
* For running Unit Tests from the commandline, run ./gradlew test
* For running Android Tests from the commandline, run ./gradlew cAT
* For debugging TransactionTooLargeExceptions, run adb logcat -s TooLargeTool
* For debugging memory leaks, run adb logcat -s LeakCanary

# Test coverage
Quite a lot of functionality and code. Covering it completely with tests would take too long,
obviously more time than specified in the requirements.
So, domain layer is covered, date and UI layers are partially covered.