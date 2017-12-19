### Android Gradle Native Plugin

依赖插件

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'io.github.lizhangqu:native-plugin:1.0.0'
    }
}

apply plugin: 'native-plugin'
```

依赖动态库

```
dependencies {
    nativeCompile "$groupId:$name:$version:$classifier"
}
```

其中classifier可选，其值为 armeabi, armeabi-v7a, arm64-v8a, x86, x86_64, mips, mips64其中一个，不是这些值会抛异常
