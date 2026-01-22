import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;



public class Evaluate {

    private FunctionStore fs;
    private List<String> semanticErrors;

    private class Function {
        public ExprNode args;
        public ExprNode expr;
        public Function(ExprNode args_, ExprNode expr_){
            args = args_;
            expr = expr_;
        }
    }
    private class Store {
        Map<String, Float> vars;
        public Store () {
            vars = new HashMap<String, Float>();
        }
        public Store (Store s) {
            vars = new HashMap<String, Float>(s.vars);
        }
        public void addVar(String name, float value){
            vars.put(name, value);
        }
        public Float getVar(String name){
            Float v = vars.get(name);
            if (v == null) {
                semanticErrors.add("Error: Free identifier "+name+" detected.");
                return 0f;
            }
            return v;
        }
    }
    private class FunctionStore {

        Map<String, Function> functions;
        public FunctionStore () {
            functions = new HashMap<String, Function>();
        }
        public void addFunction(String name, ExprNode args, ExprNode expr){
            Function f = new Function(args, expr);
            functions.put(name, f);
        }
        public Function getFunction(String name){
            Function f = functions.get(name);
            if (f == null) {
                semanticErrors.add("Error: Undefined function "+name+" detected.");
            }
            return f;
        }
    }

    private float interp_expr(ExprNode e, FunctionStore g, Store s) {
        ArrayList<ExprNode> c = e.children;
        switch (e.rule){
            case "NUM":
                return Float.parseFloat(e.value);
            case "VAR":
                return s.getVar(e.value);
            case "ADD":
                return interp_expr(c.get(0), g, s) + interp_expr(c.get(1), g, s) ;
            case "SUB":
                return interp_expr(c.get(0), g, s) - interp_expr(c.get(1), g, s) ;
            case "NEG":
                return -interp_expr(c.get(0), g, s);
            case "LetIn":
                s.addVar(c.get(0).value, interp_expr(c.get(1), g, s));
                Store ss = new Store(s);
                return interp_expr(c.get(2), g, ss);

            case "Call":
                Function f1 = g.getFunction(c.get(0).value);
                Store ss1 = new Store();
                if (f1 == null) return 0;
                if (f1.args.children.size() != 0) {
                    semanticErrors.add("Error: The number of arguments of "+c.get(0).value+" mismatched, Required: "+Integer.toString(f1.args.children.size())+", Actual: 0");
                    int size = f1.args.children.size();
                    for (int i=0; i<size; i++){
                        ss1.addVar(f1.args.children.get(i).value, 0);
                    }
                }
                FunctionStore fs1 = new FunctionStore();
                return interp_expr(f1.expr, fs1, ss1);

            case "CallParam":
                Function f2 = g.getFunction(c.get(0).value);
                if (f2 == null) return 0;
                Store ss2 = new Store();
                FunctionStore fs2 = new FunctionStore();
                int size = f2.args.children.size();

                if (size != c.get(1).children.size()) {
                    semanticErrors.add("Error: The number of arguments of "+c.get(0).value+" mismatched, Required: "+
                    Integer.toString(f2.args.children.size())+", Actual: "+Integer.toString(c.get(1).children.size()));
                    for (int i=0; i<size; i++){
                        if (i < c.get(1).children.size()) interp_expr(c.get(1).children.get(i), g, s);
                        ss2.addVar(f2.args.children.get(i).value, 0);
                    }
                }
                else {
                    for (int i=0; i<size; i++){
                        ss2.addVar(f2.args.children.get(i).value, interp_expr(c.get(1).children.get(i), g, s));
                    }
                }
                return interp_expr(f2.expr, fs2, ss2);
            default:
                //error
                return 0;
        }
    }

    private void interp_fundef (ExprNode e){
        String fname = e.children.get(0).value;
        ExprNode args = e.children.get(1);
        ExprNode expr = e.children.get(2);
        fs.addFunction(fname, args, expr);
    }

    public Evaluate(){
        fs = new FunctionStore();
        semanticErrors = new ArrayList<String>();
    }

    public Float evaluate(ExprNode parent){
        switch (parent.rule) {
            case "decl":
                interp_fundef(parent);
                return 0f;
            default:
                Store s = new Store();
                Float f =  interp_expr(parent, fs, s);

                if (semanticErrors.isEmpty()){
                    return f;
                }
                else {
                    for (String err : semanticErrors){
                        System.out.println(err);
                    }   
                    return null;
                }
        }
    }
}

