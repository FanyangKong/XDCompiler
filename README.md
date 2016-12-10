# XDCompiler
绘图语法编译器
## 语法句型
### 支持四种语法句型(大小写不敏感)
 1. OriginStatement: ORIGIN IS ( 0, 0);
 2. RotStatement: ROT IS PI;
 3. ScaleStatement: SCALE IS ( 1, 1);
 4. ForStatement: FOR T FROM 0 TO 2*PI STEP PI/20 DRAW (SIN(T), COS(T));
### 语法说明
定义原点，旋转角度，放缩比例语句具有全局作用，书写顺序对UI图效果不影响，若不改变则后文的绘图参数将一直以上文的参数为准。
## 代码说明
 1. `Scanner`
 2. `Parser`
 3. `Calculate`
 4. `ExprNode`
 5. `Token`
 5. `Log`
 6. `Token_Type`
 7. `MathMethod`
### `Scanner`类
 + `init()` 单例模式初始化词法分析器，并执行`scan()`方法，开始扫描代码文件。
 + `readFile()` 每次调用该方法会读取一行代码文件并保存在`content`变量中。
 + `isAlpha()` 判断是否为`CONST_ID`类型参数。
 + `isDigit()` 判断是否为数字。
 + `isSpace()` 判断是否为空格。
 + `printTokens()` 打印词法分析结束后的每个单词。
 + ···
### `Parser`类
 + `newInstance()` 单例模式生成唯一的语法分析器。
 + `program()` 开始语法分析。
 + `statements()` 匹配不同的语法句型(`originStatement()`, `rotStatement()`, `scaleStatement()`, `forStatement()`)。
 + `expression()` 构造表达式的分析树根据算符优先级依次有`term()`,`factor()`,`component()`,`atom()`
 + `makeExprNode()` 构造结点，此方法被重载多次。
 + `drawGraphics()` 画出`XDGraphics`图像。
 + ···
### `Calculate`类
 + `getExprValue(ExprNode root, double origin)`
 + `getExprValue(ExprNode root)`
 计算类的两个静态方法负责根据结点树计算表达式的值，此处重载两个方法的原因是Java缺乏指针，没法将变量的地址放在固定位置让循环中的数一次赋值到变量地址，只能将常数值代入到递归中去。
 // todo 去掉第一个重载的方法
### `ExprNode`类
数据结点类
### `Token`类
记号类
### `Log`类
 + `log(boolean b)` 是否开启日志打印功能
 + `enter(String name)` 打印进入name
 + `back(String name)` 打印离开name
 + `call_match(String name)` 打印匹配name
 + `tree_trace(ExprNode root, int indent)` 打印出root结点的树结构
### `Token_Type`类
结点类型枚举类
### `MathMethod`类
接口类，此接口是为了实现C++中的函数指针的效果，利用Lambda表达式简化了代码。
## 总结
设计`Scanner`类和`Parser`类的时候感觉到明显的对面向对象理解的不足，这也可能是代码实践的不够导致的，单例模式的优点没有发挥出来。虽然是课程任务但是有必要进一步修改。