import java.util.*;
import java.io.*;

public class Scanner_test{
	public static void main(String[] args) throws Exception{
		Token token;
		TokenScaner myScaner=new TokenScaner("a.txt");
		while(true){
			token=myScaner.nextToken();
			if(token.type==TokenType.NONTOKEN)
				break;
			System.out.println("< "+token.type+"    "+token.lexeme+"    "+token.value+"    line:"+token.line+" >");
		}
	}
}

enum TokenType{
	T,	//参数
	PI,	//PI
	FUNC,	//函数
	CONST_ID,	//常数
	ERRTOKEN,	//出错记号
	NONTOKEN,	//空记号
	PLUS,MINUS,MUL,DIV,POWER,	//算术运算符
	SEMICO,L_BRACKET,R_BRACKET,COMMA,	//分隔符
	ORIGIN,SCALE,ROT,IS,TO,STEP,DRAW,FOR,FROM	//保留字
};

class Token{
	TokenType type;
	String lexeme;
	double value;
	int line;

	public Token(TokenType a,String b,int c){
		type=a;
		lexeme=b;
		line=c;
	}
	public Token(TokenType a,double b,int c){
		type=a;
		value=b;
		line=c;
	}
}

class TokenScaner{
	protected LinkedList<Token> TokenTab=new LinkedList<Token>();
	protected FileInputStream fileIn;
	protected int index=0,errflag=0;
	
	public TokenType GetIdType(String str){
		String tmp=str.toUpperCase();
		if(tmp.equals("T")) return TokenType.T;
		if(tmp.equals("PI")) return TokenType.PI;
		if(tmp.equals("SIN") || tmp.equals("COS") || tmp.equals("TAN") || tmp.equals("SQRT") || tmp.equals("EXP") || tmp.equals("LN"))
			return TokenType.FUNC;
		if(tmp.equals("ORIGIN")) return TokenType.ORIGIN;
		if(tmp.equals("SCALE")) return TokenType.SCALE;
		if(tmp.equals("ROT")) return TokenType.ROT;
		if(tmp.equals("IS")) return TokenType.IS;
		if(tmp.equals("TO")) return TokenType.TO;
		if(tmp.equals("STEP")) return TokenType.STEP;
		if(tmp.equals("DRAW")) return TokenType.DRAW;
		if(tmp.equals("FOR")) return TokenType.FOR;
		if(tmp.equals("FROM")) return TokenType.FROM;
		errflag=1;
		return TokenType.ERRTOKEN;
	}
	public TokenScaner(String FilePath) throws Exception{
		char a=' ';
		Token token;
		int state=0,line=1;
		boolean readflag=true;
		String tokenBuffer="";
		boolean lineflag=false,closeflag=false;
		fileIn=new FileInputStream(FilePath);
		while(true){
			if(readflag){
				a=(char)fileIn.read();
				if(closeflag) break;
				if(a==(char)-1){ a=' ';closeflag=true;}
			}
			readflag=true;
			if(lineflag){line++;lineflag=false;}
			if(a=='\n') lineflag=true;
			switch(state){
				case -1:
					if(a=='\n')
						state=0;
					break;
				case 0:
					switch(a){
						case '+':
							token=new Token(TokenType.PLUS,"+",line);
							TokenTab.add(token); state=0; break;
						case ',':
							token=new Token(TokenType.COMMA,",",line);
							TokenTab.add(token); state=0; break;
						case ';':
							token=new Token(TokenType.SEMICO,";",line);
							TokenTab.add(token); state=0; break;
						case '(':
							token=new Token(TokenType.L_BRACKET,"(",line);
							TokenTab.add(token); state=0; break;
						case ')':
							token=new Token(TokenType.R_BRACKET,")",line);
							TokenTab.add(token); state=0; break;
						case '*':
							state=4; break;
						case '/':
							state=6; break;
						case '-':
							state=7; break;
						default:
							if((a>='a' && a<='z') || (a>='A' && a<='Z')){ state=1; tokenBuffer+=a; }
							else if(a>='0' && a<='9'){ state=2; tokenBuffer+=a; }
							else if(a!=' ' && a!='\t' && a!='\n'&& a!='\r'){
								token=new Token(TokenType.ERRTOKEN,""+a,line);
								TokenTab.add(token);errflag=1;
							}
					}
					break;
				case 1:
					if((a>='a' && a<='z') || (a>='A' && a<='Z'))
					       	tokenBuffer+=a;
					else{
						token=new Token(GetIdType(tokenBuffer),tokenBuffer,line);
						TokenTab.add(token); tokenBuffer=""; readflag=false; state=0;
					}
					break;
				case 2:
					if(a=='.'){ tokenBuffer+=a; state=3; }
					else if(a>='0' && a<='9') tokenBuffer+=a;
					else{
						token=new Token(TokenType.CONST_ID,Double.parseDouble(tokenBuffer),line);
						TokenTab.add(token); tokenBuffer=""; readflag=false; state=0;
					}
					break;
				case 3:
					if(a>='0' && a<='9') tokenBuffer+=a;
					else{
						token=new Token(TokenType.CONST_ID,Double.parseDouble(tokenBuffer),line);
						TokenTab.add(token); tokenBuffer=""; readflag=false; state=0;
					}
					break;	
				case 4:
					if(a=='*'){
						token=new Token(TokenType.POWER,"**",line);
						TokenTab.add(token);tokenBuffer="";state=0;
					}
					else{
						token=new Token(TokenType.MUL,"*",line);
						TokenTab.add(token);tokenBuffer="";state=0;readflag=false;
					}
					break;
				case 6:
					if(a=='/')
						state=-1;
					else{
						token=new Token(TokenType.DIV,"/",line);
						TokenTab.add(token);state=0;readflag=false;
					}
					break;
				case 7:
					if(a=='-')
						state=-1;
					else{
						token=new Token(TokenType.MINUS,"-",line);
						TokenTab.add(token);state=0;readflag=false;
					}
					break;
			}
		}
		fileIn.close();
		token=new Token(TokenType.NONTOKEN,"",line-1);
		TokenTab.add(token);
	}

	public Token nextToken(){
		if(TokenTab.get(index).type==TokenType.NONTOKEN)
			return TokenTab.get(index);
		else{
			index++;
			return TokenTab.get(index-1);
		}
	}

	public Token preToken(){
		return TokenTab.get(index-2);
	}
}
