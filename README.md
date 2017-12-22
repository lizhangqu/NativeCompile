### Android Gradle Native Compile Plugin

依赖插件

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'io.github.lizhangqu:native-compile-plugin:1.0.0'
    }
}

apply plugin: 'native-compile-plugin'

nativeCompile {
    defaultClassifier 'armeabi' //可选配置，如果依赖中没有指定classifier，则拷贝到此abi目录
}
```

依赖动态库

```
dependencies {
    nativeCompile "$groupId:$name:$version:$classifier@so"
}
```

其中classifier可选，其值为 armeabi, armeabi-v7a, arm64-v8a, x86, x86_64, mips, mips64其中一个，不是这些值会抛异常。
并且依赖中的ext @so是否需要携带取决于发布时默认的文件是否是so，即packaging是否为so。如果存在classifier, 则@so为必选项，默认值为@jar，为了让其寻找so，需要手动指定为@so。
不支持引入所有abi，只支持单个abi逐个引入

如

```
dependencies {
    nativeCompile 'com.snappydb:snappydb-native:0.2.0:armeabi@so'
    nativeCompile 'com.snappydb:snappydb-native:0.2.0:x86@so'
    nativeCompile 'com.snappydb:snappydb-native:0.2.0:mips@so'
    nativeCompile "com.snappydb:snappydb-native:0.2.0:armeabi-v7a@so"
}
```

如果依赖了非.so的远程库，则构建过程不会发生异常，但是该文件会被忽略，如

```
dependencies {
    nativeCompile 'com.android.support:appcompat-v7:26.1.0'
}
```

在评估配置完成后，nativeCompile依赖会将对应的so拷贝到相应的jniLibs下，命名方式为libname.so，如果对应的文件是以lib开头，则不添加lib前缀


当前不支持将动态库拷贝到buildType或者flavor下的目录，需求强烈后续考虑添加