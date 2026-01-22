import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

public class BuildAstVisitor extends ExprBaseVisitor<ExprNode> {

    @Override
	public ExprNode visitProg(ExprParser.ProgContext ctx)
    {
        ExprNode result = new ExprNode("Prog");
        ExprNode fundefs = visit(ctx.getChild(0));
        for (ExprNode fun : fundefs.children){
            result.addChild(fun);
        }
        result.addChild(visit(ctx.getChild(1)));
        return result;
    }     
    /*
    @Override
    public ExprNode visitAssign(ExprParser.AssignContext ctx)
    {
        ExprNode result = new ExprNode("ASSIGN");
        String var_name = ctx.getChild(0).getText();
        ExprNode variable = new ExprNode(var_name);
        result.addChild(variable);

        result.addChild(visit(ctx.getChild(2)));

        return result;
    }
     */
    
	public ExprNode visitDecl_list(ExprParser.Decl_listContext ctx)
    {
        ExprNode result = new ExprNode("decl_list");
        result.rule = "decl_list";
        if (ctx.children == null) return result;
        for(ParseTree c : ctx.children){
            ExprNode vc = visit(c);
            if(vc == null){
                continue;
            }
            result.addChild(vc);
        }
        return result;
    }

	public ExprNode visitDecl(ExprParser.DeclContext ctx)
    {
        ExprNode result = new ExprNode("decl");
        result.rule = "decl";
        result.addChild(visit(ctx.getChild(1)));
        result.addChild(visit(ctx.getChild(2)));
        result.addChild(visit(ctx.getChild(4)));
        return result;
    }

	public ExprNode visitVar_list(ExprParser.Var_listContext ctx)
	{
        ExprNode result = new ExprNode("var_list");
        result.rule = "var_list";
        if (ctx.children == null) return result;
        for(ParseTree c : ctx.children){
            ExprNode vc = visit(c);
            if(vc == null){
                continue;
            }
            result.addChild(vc);
        }
        return result;
    }

    public ExprNode visitLetinExpr(ExprParser.LetinExprContext ctx)
    {
        ExprNode result = new ExprNode("LETIN");
        result.rule = "LetIn";
        result.addChild(visit(ctx.getChild(1)));
        result.addChild(visit(ctx.getChild(3)));
        result.addChild(visit(ctx.getChild(5)));
        return result;
    }

    public ExprNode visitVarfunExpr(ExprParser.VarfunExprContext ctx)
    {
        ExprNode result = new ExprNode("Call");
        result.rule = "Call";
        result.addChild(visit(ctx.getChild(0)));
        return result;
    }

	public ExprNode visitVarfunparExpr(ExprParser.VarfunparExprContext ctx)
	{
        ExprNode result = new ExprNode("Call");
        result.rule = "CallParam";
        result.addChild(visit(ctx.getChild(0)));
        result.addChild(visit(ctx.getChild(2)));
        return result;
    }

    @Override
	public ExprNode visitInfixExpr(ExprParser.InfixExprContext ctx)
    {
        ExprNode result = new ExprNode("");
        ParseTree l = ctx.getChild(0);
        result.addChild(visit(l));
        
        ParseTree c = ctx.getChild(1);
        String val = c.getText();

        switch (val) {
            case "+":   val = "ADD";
                        break;
            case "-":   val = "SUB";
                        break;
            case "*":   val = "MUL";
                        break;
            case "/":   val = "DIV";
                        break;
            default:
                break;
        }
        result.value = val;
        result.rule = val;

        ParseTree r = ctx.getChild(2);
        result.addChild(visit(r));
        return result;
    }

    @Override
	public ExprNode visitNumberExpr(ExprParser.NumberExprContext ctx)
    {
        ParseTree child = ctx.getChild(0);
        return visit(child);
    }

    @Override
	public ExprNode visitParensExpr(ExprParser.ParensExprContext ctx)
    {
        return visit(ctx.getChild(1));
    }

	public ExprNode visitNegExpr(ExprParser.NegExprContext ctx)
	{
        ExprNode result = new ExprNode("NEGATE");
        result.rule = "NEG";
        result.addChild(visit(ctx.getChild(2)));
        return result;
    }

	public ExprNode visitExpr_list(ExprParser.Expr_listContext ctx)
	{
        ExprNode result = new ExprNode("expr_list");
        result.rule = "expr_list";
        if (ctx.children == null) return result;
        for(ParseTree c : ctx.children){
            ExprNode vc = visit(c);
            if(vc == null){
                continue;
            }
            result.addChild(vc);
        }
        
        return result;
    }
    @Override
	public ExprNode visitVarExpr(ExprParser.VarExprContext ctx)
    {
        ParseTree child = ctx.getChild(0);
        return visit(child);
    }
    @Override
	public ExprNode visitVar(ExprParser.VarContext ctx)
    {
        String a = ctx.getText();
        ExprNode result = new ExprNode(a);
        result.value = a;
        result.rule = "VAR";
        return result;
    }
    
    @Override
	public ExprNode visitNum(ExprParser.NumContext ctx)
    {
        String a = ctx.getText();
        ExprNode result = new ExprNode(a);
        result.value = a;
        result.rule = "NUM";
        return result;
    }
}