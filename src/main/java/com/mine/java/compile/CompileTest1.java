package com.mine.java.compile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import javax.script.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * @author CaoY
 * @date 2023-07-11 21:22
 * @description 脚本、编译与注解处理 - Java 平台的脚本机制
 */
public class CompileTest1 {

    @Test
    public void test1() {
        ScriptEngineManager manager = new ScriptEngineManager();
        // 获取脚本引擎
        ScriptEngine engine = manager.getEngineByName("nashorn");
        System.out.println(engine);

        // 返回该脚本引擎的工厂对象
        ScriptEngineFactory factory = engine.getFactory();
        System.out.println(factory);

        List<String> names = factory.getNames();
        System.out.println("该工厂所了解的名字：" + names);
        List<String> extensions = factory.getExtensions();
        System.out.println("扩展名：" + extensions);
        List<String> mimeTypes = factory.getMimeTypes();
        System.out.println("该工厂的Mime类型：" + mimeTypes);

        // 获取在多个线程中并发执行脚本是否安全 null - 不安全
        Object param = factory.getParameter("THREADING");
        System.out.println(param);
    }

    /**
     * 1. Bindings类的主要意义是提供了一种在脚本引擎和Java应用程序之间传递数据的机制。它允许将变量绑定到脚本环境中，
     * 并在脚本执行期间保持状态。通过Bindings，Java代码可以与脚本语言进行数据交互，
     * 实现了在不同语言之间的桥接和数据共享。
     * 2. 与ScriptEngineManager和ScriptEngine相比，Bindings的作用略有不同：
     * 3. ScriptEngineManager是用于管理脚本引擎的工具类。它负责查找和获取可用的脚本引擎实例。
     * 你可以使用ScriptEngineManager获取适合特定脚本语言的ScriptEngine实例。
     * 4. ScriptEngine是Java中的脚本引擎接口。它定义了与脚本引擎交互的方法，如执行脚本、设置变量、获取执行结果等。
     * 通过ScriptEngine，你可以将脚本代码传递给脚本引擎执行，并获取执行结果。
     * 5. Bindings则是ScriptEngine接口的一部分，用于在Java代码和脚本之间传递数据。
     * Bindings提供了一种将变量绑定到脚本环境的机制，使得Java代码和脚本之间可以共享变量和数据。
     * 你可以使用Bindings对象的方法来添加、获取和删除绑定的变量，以及在脚本执行期间保持状态。
     * 6. 因此，Bindings与ScriptEngineManager和ScriptEngine具有不同的功能。Bindings关注于数据传递和共享，
     * 而ScriptEngineManager和ScriptEngine更关注于管理和执行脚本引擎。它们在不同层面上协同工作，
     * 使得Java和脚本语言之间的交互变得更加灵活和强大。
     * @throws ScriptException
     */
    @Test
    public void test2() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        engine.eval("n = 1728");
        Object res = engine.eval("n + 1");
        System.out.println(res);

        // 向引擎中添加新的变量绑定
        engine.put("k", 1234);
        Object res2 = engine.eval("k + 1");
        System.out.println("计算后 k 的值为：" + res2);

        // 获取有脚本绑定的值
        Object k = engine.get("k");
        System.out.println("绑定的 k 的值为：" + k);

        // 向 manager 中添加值相当于添加到全局作用域中，对于所有引擎都是可见的
        manager.put("all", "你好");
        Object all = engine.get("all");
        System.out.println("添加到全局作用域的值：" + all);

        ScriptEngine engine1 = manager.getEngineByName("nashorn");
        System.out.println("对同一个 manager 多次获取同一类执行引擎是否单例：" + ((engine == engine1 ? "是" : "否")));

        Bindings bindings = engine.createBindings();
        Person p = new Person("小明", 21, "男");
        bindings.put("p", p);
        Person person = (Person) bindings.get("p");
        System.out.println(person);
    }

    // 重定向输入和输出，重定向到一个对象是方便我们后面对这个对象的控制，可是，由于我对这一块的不熟悉，并没有
    // 利用下面的例子实现这个目的。
    @Test
    public void test3() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        StringWriter writer = new StringWriter();

        // Define console.log function for the Nashorn script engine
        engine.getContext().getBindings(ScriptContext.ENGINE_SCOPE).put("console", new Console());

        // Redirect script's standard output to the StringWriter
        engine.getContext().setWriter(new PrintWriter(writer, true));

        // 重定向脚本的标准输出
        engine.getContext().setWriter(new PrintWriter(writer, true));
        engine.eval("console.log('Hello,World!')");

        String s = writer.toString();
        System.out.println(s.length());
    }

    // 调用脚本语言的函数
    @Test
    public void test4() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        // 定义脚本中的函数
        engine.eval("function greet(how, whom) {return how + ', ' + whom + '!'}");

        // 调用脚本中的函数
        String result = (String) ((Invocable) engine).invokeFunction("greet", "Hello", "Mike");
        System.out.println(result);
    }

    // 如果脚本语言是面向对象的，那么就可以调用 invokeMethod
    @Test
    public void test5() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        // 定义一个 Greeter 类
        engine.eval("function Greeter(how) {this.how = how}");
        engine.eval("Greeter.prototype.welcome = function(whom) {return this.how + ', ' + whom + '!'}");

        // 构造一个 Greeter 对象
        Object ok = engine.eval("new Greeter('OK')");
        // 调用 welcome 方法
        Object result = ((Invocable) engine).invokeMethod(ok, "welcome", "Bob");
        Object result2 = ((Invocable) engine).invokeMethod(ok, "welcome", "Jerry");
        System.out.println(result);
        System.out.println(result2);
    }

    // 在脚本中实现 Java 中的接口
    @Test
    public void test6() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");

        // 如果在 nashorn 执行引擎中定义了与接口中的方法相同名字的函数，
        // 那么就可以用接口中的方法来调用脚本中的函数，这里的脚本中的方法
        // 的个数和名字以及参数状况一定要与 Java 接口中的方法的保持一致。
        engine.eval("function welcome(whom) {return 'welcome, ' + whom + '!'}");
        engine.eval("function hello(whom) {return 'hello, ' + whom + '!'}");

        // 获取 Greeter 对象
        Greeter greeter = ((Invocable) engine).getInterface(Greeter.class);
        String str = greeter.welcome("David");
        System.out.println(str);
    }

    public interface Greeter {
        String welcome(String whom);
        String hello(String whom);
    }

    public static class Console {
        public void log(Object object) {
            System.out.println(object);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Person {
        private String name;
        private Integer age;
        private String gender;
    }
}
