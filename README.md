# plotting Interpreter

## 说明

该项目为《编译原理》课程作业”实习一个简单绘图程序的解释器“。  

绘图程序的语法要求如下：

![image1](https://raw.githubusercontent.com/a-psycho/plottingInterpreter/master/images/image1.png)

本实验采用java编写

## 运行截图

![run_example](https://raw.githubusercontent.com/a-psycho/plottingInterpreter/master/images/run_example.png)

## 词法分析器

1. 各个记号的正规式

![image2](https://raw.githubusercontent.com/a-psycho/plottingInterpreter/master/images/image2.png)

2. 求正规式的 DFA

   ![image3](https://raw.githubusercontent.com/a-psycho/plottingInterpreter/master/images//image3.png)

3. 按照正规式的DFA编写代码即可

## 语法分析器

1. 写出各语句的文法并依次改写为无二义文法，然后消除左递归和提取左因子

![image4](https://raw.githubusercontent.com/a-psycho/plottingInterpreter/master/images/image4.png)

![image5](https://raw.githubusercontent.com/a-psycho/plottingInterpreter/master/images/image5.png)

2. 编写需要的递归子程序

![image6](https://raw.githubusercontent.com/a-psycho/plottingInterpreter/master/images/image6.png)
