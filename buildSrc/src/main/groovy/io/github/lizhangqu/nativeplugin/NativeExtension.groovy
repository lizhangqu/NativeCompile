package io.github.lizhangqu.nativeplugin

import org.gradle.api.Project

class NativeExtension {

    //默认值
    static final String DEFAULT_CLASSIFIER = "armeabi"
    //支持的abi列表
    static final List<String> ABI = Arrays.asList("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64", "mips", "mips64")

    Project project

    String classifier = DEFAULT_CLASSIFIER

    NativeExtension(Project project) {
        this.project = project
    }

    void classifier(String classifier) {
        //校验abi
        if (!ABI.contains(classifier)) {
            throw new IllegalArgumentException("classifier must be in ${ABI}")
        }
        this.classifier = classifier
    }

}
