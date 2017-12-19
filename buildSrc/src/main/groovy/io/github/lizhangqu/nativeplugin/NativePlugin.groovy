package io.github.lizhangqu.nativeplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.file.CopySpec
import org.gradle.api.file.FileCollection
import org.gradle.util.GFileUtils

class NativePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        //create extension
        project.getExtensions().create("nativePlugin", NativeExtension.class, project)
        Configuration nativeCompileConfiguration = project.getConfigurations().create("nativeCompile") { Configuration nativeCompileConfiguration ->
            //禁止传递依赖
            nativeCompileConfiguration.setTransitive(false)
            nativeCompileConfiguration.resolutionStrategy {
                cacheChangingModulesFor(0, 'seconds')
                cacheDynamicVersionsFor(5, 'minutes')
            }
        }
        project.afterEvaluate {
            NativeExtension nativeExtension = project.getExtensions().findByType(NativeExtension.class)
            nativeCompileConfiguration.getDependencies().each { Dependency nativeDependency ->
                FileCollection collection = nativeCompileConfiguration.fileCollection(nativeDependency).filter { File file ->
                    //返回so文件
//                    return file.getName().endsWith(".so")
                    return true
                }
                //遍历
                collection.files.each { File srcFile ->
                    //文件后缀
                    String suffix = srcFile.getName().substring(srcFile.getName().lastIndexOf("."))
                    //依赖classifier
                    String classifier = srcFile.getName() - nativeDependency.getName() - "-" - nativeDependency.getVersion() - "-" - suffix
                    //如果classifier为空，则默认使用armeabi
                    if (classifier == null || classifier.length() == 0) {
                        classifier = nativeExtension.classifier
                    }
                    //目标目录
                    File destDir = project.file("src/main/jniLibs/${classifier}/")
                    //目标文件名
                    String destFileName = "${nativeDependency.getName().startsWith('lib') ? '' : 'lib'}${nativeDependency.getName()}.so"
                    //删除旧文件
                    GFileUtils.deleteQuietly(new File(destDir, destFileName))
                    //拷贝源文件到目标
                    project.copy { CopySpec copySpec ->
                        copySpec.from(srcFile)
                        copySpec.into(destDir)
                        copySpec.rename(srcFile.getName(), destFileName)
                    }
                }
            }
        }
    }
}