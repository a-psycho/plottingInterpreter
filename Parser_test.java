import java.util.*;

public class Parser_test{
	public static void main(String[] args) throws Exception{
		int m=0;
		StatementTree tmpTree=null;
		Parser myparser=new Parser("a.txt");
		while((tmpTree=myparser.nextStatementTree()).sType!=StatementType.NONSTATEMENT){
			if(tmpTree.sType==StatementType.ORIGINSTATEMENT) m=2;
			else if(tmpTree.sType==StatementType.SCALESTATEMENT) m=2;
			else if(tmpTree.sType==StatementType.ROTSTATEMENT) m=1;
			else m=5;
			System.out.println(tmpTree.sType);
			for(int i=0;i<m;i++){
				tmpTree.root[i].printExpression(1);
				System.out.print("\n");
				//System.out.println(tmpTree.root[i].getExprAnswer(0));
			}
		}
	}
}

class ExprNode{
	Token exprToken;
	ExprNode lNode=null,rNode=null;

	public ExprNode(Token a){
		exprToken=a;
	}

	public double getExprAnswer(double t){	
		if(exprToken.type==TokenType.PLUS){
			if(lNode!=null)
				return lNode.getExprAnswer(t)+rNode.getExprAnswer(t);
			else
				return rNode.getExprAnswer(t);
		}
		if(exprToken.type==TokenType.MINUS){
			if(lNode!=null)
				return lNode.getExprAnswer(t)-rNode.getExprAnswer(t);
			else
				return 0.0-rNode.getExprAnswer(t);
		}
		if(exprToken.type==TokenType.MUL)
			return lNode.getExprAnswer(t)*rNode.getExprAnswer(t);
		if(exprToken.type==TokenType.DIV)
			return lNode.getExprAnswer(t)/rNode.getExprAnswer(t);
		if(exprToken.type==TokenType.POWER)
			return Math.pow(lNode.getExprAnswer(t),rNode.getExprAnswer(t));
		if(exprToken.type==TokenType.CONST_ID)
			return exprToken.value;
		if(exprToken.type==TokenType.T)
			return t;
		if(exprToken.type==TokenType.PI)
			return Math.PI;
		if(exprToken.type==TokenType.FUNC){
			if(exprToken.lexeme.compareToIgnoreCase("cos")==0)
				return Math.cos(lNode.getExprAnswer(t));
			else if(exprToken.lexeme.compareToIgnoreCase("sin")==0)
				return Math.sin(lNode.getExprAnswer(t));
			else if(exprToken.lexeme.compareToIgnoreCase("tan")==0)
				return Math.tan(lNode.getExprAnswer(t));
			else if(exprToken.lexeme.compareToIgnoreCase("exp")==0)
				return Math.exp(lNode.getExprAnswer(t));
			else if(exprToken.lexeme.compareToIgnoreCase("sqrt")==0)
				return Math.sqrt(lNode.getExprAnswer(t));
			else
				return Math.log(lNode.getExprAnswer(t));
		}
		return 0.0;
	}

	public void printExpression(int n){
		for(int i=0;i<n;i++)
			System.out.print("    ");
		if(exprToken.type!=TokenType.CONST_ID)
			System.out.println(exprToken.lexeme);
		else
			System.out.println(exprToken.value);
		if(lNode!=null)
			lNode.printExpression(n+1);
		if(rNode!=null)
			rNode.printExpression(n+1);
	}
}

enum StatementType{
	ORIGINSTATEMENT,SCALESTATEMENT,
	ROTSTATEMENT,FORSTATEMENT,NONSTATEMENT
};
class StatementTree{
	StatementType sType;
	ExprNode[] root=new ExprNode[5];
}

class Parser{
	protected int index=0;
	protected Token token;
	protected boolean tAllowFlag=false;
	protected TokenScaner myscaner;
	LinkedList <StatementTree> stTab=new LinkedList<StatementTree>();
	
	public Parser(String FilePath) throws Exception{
		myscaner=new TokenScaner(FilePath);
		if(myscaner.errflag==1){
			System.out.println("Token Error:");
			while(true){
				token=myscaner.nextToken();
				if(token.type==TokenType.NONTOKEN) break;
				if(token.type==TokenType.ERRTOKEN)
					System.out.println("< "+token.type+"    "+token.lexeme+"    "+token.value+"    line:"+token.line+" >");
			}
			System.exit(-1);
		}
		program();
	}

	public StatementTree nextStatementTree(){
		index++;
		return stTab.get(index-1);
	}

	boolean match(TokenType tType,String str,boolean errFlag){
		if(token.type==tType){
			token=myscaner.nextToken();
			return true;
		}
		if(errFlag){
			System.out.println("Grammar Error:");
			if(token.type!=TokenType.CONST_ID)
				System.out.println("line<"+token.line+">: Unexpected token '"+token.lexeme+"'; "+str);
			else
				System.out.println("line<"+token.line+">: Unexpected token '"+token.value+"'; "+str);
			System.exit(-1);
		}
		return false;
	}

	void program(){
		token=myscaner.nextToken();
		while(token.type!=TokenType.NONTOKEN){
			stTab.add(statement());
			match(TokenType.SEMICO,"Token ';' is expected!",true);
		}
		StatementTree tmp=new StatementTree();
		tmp.sType=StatementType.NONSTATEMENT;
		stTab.add(tmp);
	}

	StatementTree statement(){
		StatementTree tmp=new StatementTree();
		if(match(TokenType.ORIGIN,null,false)){
			match(TokenType.IS,"Token 'IS' is expected!",true);
			match(TokenType.L_BRACKET,"Token '(' is expected!",true);
			tmp.root[0]=expression();
			match(TokenType.COMMA,"Token ',' is expected!",true);
			tmp.root[1]=expression();
			match(TokenType.R_BRACKET,"Token ')' is expected!",true);
			tmp.sType=StatementType.ORIGINSTATEMENT;
		}
		else if(match(TokenType.ROT,null,false)){
			match(TokenType.IS,"Token 'IS' is expected!",true);
			tmp.root[0]=expression();
			tmp.sType=StatementType.ROTSTATEMENT;
		}
		else if(match(TokenType.SCALE,null,false)){
			match(TokenType.IS,"Token 'IS' is expected!",true);
			match(TokenType.L_BRACKET,"Token '(' is expected!",true);
			tmp.root[0]=expression();
			match(TokenType.COMMA,"Token ',' is expected!",true);
			tmp.root[1]=expression();
			match(TokenType.R_BRACKET,"Token ')' is expected!",true);
			tmp.sType=StatementType.SCALESTATEMENT;
		}
		else if(match(TokenType.FOR,null,false)){
			match(TokenType.T,"Token 'T' is expected!",true);
			match(TokenType.FROM,"Token 'FROM' is expected!",true);
			tmp.root[0]=expression();
			match(TokenType.TO,"Token 'TO' is expected!",true);
			tmp.root[1]=expression();
			match(TokenType.STEP,"Token 'STEP' is expected!",true);
			tmp.root[2]=expression();
			tAllowFlag=true;
			match(TokenType.DRAW,"Token 'DRAW' is expected!",true);
			match(TokenType.L_BRACKET,"Token '(' is expected!",true);
			tmp.root[3]=expression();
			match(TokenType.COMMA,"Token ',' is expected!",true);
			tmp.root[4]=expression();
			match(TokenType.R_BRACKET,"Token ')' is expected!",true);
			tAllowFlag=false;
			tmp.sType=StatementType.FORSTATEMENT;
		}
		else
			match(TokenType.FOR,"You should start with 'ORIGIN'/'ROT'/'SCALE'/'FOR' !",true);
		return tmp;
	}

	ExprNode expression(){
		ExprNode tmpa,tmpb;
		tmpa=term();
		while(token.type==TokenType.PLUS || token.type==TokenType.MINUS){
			if(!match(TokenType.PLUS,null,false))
				match(TokenType.MINUS,null,false);
			tmpb=new ExprNode(myscaner.preToken());
			tmpb.lNode=tmpa;
			tmpb.rNode=term();
			tmpa=tmpb;
		}
		return tmpa;
	}

	ExprNode term(){
		ExprNode tmpa,tmpb;
		tmpa=factor();
		while(token.type==TokenType.MUL || token.type==TokenType.DIV){
			if(!match(TokenType.MUL,null,false))
				match(TokenType.DIV,null,false);
			tmpb=new ExprNode(myscaner.preToken());
			tmpb.lNode=tmpa;
			tmpb.rNode=factor();
			tmpa=tmpb;
		}
		return tmpa;
	}

	ExprNode factor(){
		ExprNode tmp;
		if((!match(TokenType.PLUS,null,false)) && (!match(TokenType.MINUS,null,false)))
			return component();
		tmp=new ExprNode(myscaner.preToken());
		tmp.rNode=factor();
		return tmp;
	}

	ExprNode component(){
		ExprNode tmpa,tmpb;
		tmpa=atom();
		if(match(TokenType.POWER,null,false)){
			tmpb=new ExprNode(myscaner.preToken());
			tmpb.lNode=tmpa;
			tmpb.rNode=component();
			tmpa=tmpb;
		}
		return tmpa;
	}

	ExprNode atom(){
		ExprNode tmp=null;
		if(match(TokenType.CONST_ID,null,false))
			tmp=new ExprNode(myscaner.preToken());
		else if(match(TokenType.PI,null,false))
			tmp=new ExprNode(myscaner.preToken());
		else if(match(TokenType.T,null,false)){
			if(!tAllowFlag)
				match(TokenType.T,"Expression here can't use 'T'!",true);
			tmp=new ExprNode(myscaner.preToken());
		}
		else if(match(TokenType.FUNC,null,false)){
			tmp=new ExprNode(myscaner.preToken());
			match(TokenType.L_BRACKET,"Token '(' is expected!",true);
			tmp.lNode=expression();
			match(TokenType.R_BRACKET,"Token ')' is expected!",true);
		}
		else if(match(TokenType.L_BRACKET,null,false)){
			tmp=expression();
			match(TokenType.R_BRACKET,"Token ')' is expected!",true);
		}
		else
			match(TokenType.CONST_ID,"You should input CONST_ID/T/FUNC(EXPRESSION)/(EXPRESSION) here!",true);
		return tmp;
	}
}
