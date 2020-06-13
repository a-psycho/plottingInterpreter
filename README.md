# plotting Interpreter

## 说明

该项目为《编译原理》课程作业”实习一个简单绘图程序的解释器“。  

绘图程序的语法要求如下：

![image1](.\images\image1.png)

本实验采用java编写

## 运行截图

![](https://github.com/a-psycho/plottingInterpreter/blob/tree/master/images/run_example.png)

## 词法分析器

1. 各个记号的正规式

![](https://github.com/a-psycho/plottingInterpreter/blob/tree/master/images/image2.jpeg.png)

2. 求正规式的 DFA

   ![](https://github.com/a-psycho/plottingInterpreter/blob/master/images/image3.jpeg.png)

3. 按照正规式的DFA编写代码即可

## 语法分析器

1. 写出各语句的文法并依次改写为无二义文法，然后消除左递归和提取左因子

![](https://github.com/a-psycho/plottingInterpreter/blob/master/images/image4.jpeg.png)

![](https://github.com/a-psycho/plottingInterpreter/blob/master/images/image5.jpeg.png)

2. 编写需要的递归子程序

![](https://github.com/a-psycho/plottingInterpreter/blob/master/images/image6.jpeg.png)
