
public class AstCall {
    private int white_space = 10;
    public AstCall(){

    }   
    public void Call(ExprNode parent, int a){
        if (parent == null) return;
        
        if (parent.rule == "var_list" || parent.rule == "decl_list" ||  parent.rule == "expr_list") {
            parent.children.forEach(node -> Call(node, a));
            return;
        }
        float f = 0;
        System.out.print(" ".repeat(a * white_space));
        try {
            f = Float.parseFloat(parent.value);
            System.out.println(f);
        }
        catch (Exception e){
            System.out.println(parent.value);
        }
        parent.children.forEach(node -> Call(node, a+1));
    }
}