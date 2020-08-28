package com.example.compiler;

import com.example.annotation.Route;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(Route.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Route.class);
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            Route annotation = element.getAnnotation(Route.class);
            String path = annotation.path();

            TypeElement typeElement = (TypeElement) element;
            String s = typeElement.getQualifiedName().toString();
//            ClassName className = ClassName.get(typeElement);
//            String qn = typeElement.getQualifiedName().toString();
//            String sn = typeElement.getSimpleName().toString();
//            String packageName = qn.replace("." + sn, "");
            map.put(path, s);
        }
        generateFile(map);


        return true;
    }

    private void generateFile(Map<String, String> map) {
        if (map.size() > 0) {
            String ITROUTE_ROOT = "com.example.hsrouter" + ".IRouteRoot";
            TypeElement type_IRouteRoot = elementUtils.getTypeElement(ITROUTE_ROOT);
            ParameterizedTypeName inputMapTypeOfRoot = ParameterizedTypeName.get(
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ParameterizedTypeName.get(
                            ClassName.get(Class.class),
                            TypeVariableName.get("?")/*
                        WildcardTypeName.subtypeOf(ClassName.get(type_IRouteRoot))*/
                    )
            );

            ParameterSpec rootParamSpec = ParameterSpec.builder(inputMapTypeOfRoot, "routes").build();

            MethodSpec.Builder loadIntoMethodOfRootBuilder = MethodSpec.methodBuilder("loadInto")
                    .addAnnotation(Override.class)
                    .addModifiers(PUBLIC)
                    .addParameter(rootParamSpec);

            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String path = entry.getKey();
                String s = entry.getValue();
                int index = s.lastIndexOf(".");
                String packageName = s.substring(0, index);
                String simpleName = s.substring(index+1);
                loadIntoMethodOfRootBuilder.addStatement("routes.put($S, $T.class)", path, ClassName.get(packageName, simpleName));
            }

            TypeSpec generateClass = TypeSpec.classBuilder("ARouter$$" + "Group$$" + System.currentTimeMillis())
                    .addModifiers(PUBLIC)
                    .addSuperinterface(/*IRouteRoot.class*/ClassName.get(type_IRouteRoot))
                    .addMethod(loadIntoMethodOfRootBuilder.build())
                    .build();

            JavaFile javaFile = JavaFile.builder("com.hello.test", generateClass).build();

            try {
                javaFile.writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印日志
     *
     * @param kind
     * @param message
     */
    private void printMsg(Diagnostic.Kind kind, String message) {
        messager.printMessage(kind, message);
    }
}
