package io.github.lizhangqu.nativecompile.plugin

import org.gradle.api.Project

class NativeCompileExtension {

    //默认值
    static final String DEFAULT_CLASSIFIER = "armeabi"
    //支持的abi列表
    static
    final List<String> ABI = Arrays.asList("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64", "mips", "mips64")

    Project project

    String defaultClassifier = DEFAULT_CLASSIFIER

    NativeCompileExtension(Project project) {
        this.project = project
    }

    void defaultClassifier(String defaultClassifier) {
        //校验abi
        if (!ABI.contains(defaultClassifier)) {
            throw new IllegalArgumentException("defaultClassifier must be in ${ABI}")
        }
        this.defaultClassifier = defaultClassifier
    }

}
