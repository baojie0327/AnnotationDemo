package com.jackson.bindviewcompiler;


import com.google.auto.service.AutoService;
import com.jackson.bindviewannotation.BindView;


import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 这个注解用于生成Java文件
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    // 文件相关辅助类
    private Filer mFiler;
    // 日志相关辅助类
    private Messager mMessager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations=new LinkedHashSet<>();
        annotations.add(BindView.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        Map<TypeElement,ArrayList<BindViewInfo>> bindViewMap=new HashMap<>();
        for (Element element : elements) {
            // 因为BindView只作用于Filed，判断注解是否是属性，不是的话直接结束
            if (element.getKind() != ElementKind.FIELD) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, element.getSimpleName().toString() + " is not filed,can not use @BindView");
                return false;
            }
            // 获取注解元数据
            int id = element.getAnnotation(BindView.class).value();
            // 获取属性的类
            TypeElement typeElement= (TypeElement) element.getEnclosingElement();

            if (!bindViewMap.containsKey(typeElement)){
                bindViewMap.put(typeElement,new ArrayList<BindViewInfo>());
            }

            ArrayList<BindViewInfo> bindViewInfos=bindViewMap.get(typeElement);
            // 添加list
            bindViewInfos.add(new BindViewInfo(id,element.getSimpleName().toString()));

        }
        produceClass(bindViewMap);
        return true;
    }


    private void produceClass(Map<TypeElement,ArrayList<BindViewInfo>> hasMap){

        if (hasMap == null || hasMap.isEmpty()){
            return;
        }

        Set<TypeElement> typeElements=hasMap.keySet();
        for (TypeElement typeElement:typeElements){
            produceJavaClass(typeElement,hasMap.get(typeElement));
        }

    }


    /**
     * 产生Java文件
     * @param typeElement
     * @param bindViewInfos
     */
    private void produceJavaClass(TypeElement typeElement, List<BindViewInfo> bindViewInfos){

        try {
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("package ");
            stringBuffer.append(getPackageName(typeElement.getQualifiedName().toString())+";\n");
            stringBuffer.append("import com.jackson.bindviewapi.IViewBind;\n");
            stringBuffer.append("public class "+typeElement.getSimpleName()+"$$ViewBinder< T extends "+typeElement.getSimpleName()+"> implements IViewBind<T> {\n");
            stringBuffer.append("@Override\n");
            stringBuffer.append("public void bind(T activity) {\n");

            for (BindViewInfo bindViewInfo:bindViewInfos){
                stringBuffer.append("activity."+bindViewInfo.name+"=activity.findViewById("+bindViewInfo.id+");\n");
            }
            stringBuffer.append("}\n}");
            JavaFileObject javaFileObject=mFiler.createSourceFile(typeElement.getQualifiedName().toString()+"$$ViewBinder");
            Writer writer=javaFileObject.openWriter();
            writer.write(stringBuffer.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getPackageName(String className){
        if (className==null || className.equals("")){
            return "";
        }

        return className.substring(0,className.lastIndexOf("."));
    }




}
