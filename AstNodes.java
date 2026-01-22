import java.util.ArrayList;

class ExprTree {
    
    public ArrayList<ExprNode> children;
    public ExprNode parentsNode;
    public String value;
    public String rule;

    public ExprTree(String s) {
        children = new ArrayList<ExprNode>();
        value = s;
    }

    public void addChild(ExprNode child) {
        children.add(child);
    }
}
class ExprNode extends ExprTree{
    public String root;
    public ExprNode(String s){
        super(s);
    }
}