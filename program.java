import java.io.IOException;
import org.antlr.v4.runtime.*;
import java.util.ArrayList;

public class program {

    public static void main(String[] args) throws IOException {
                
        // Get Lexer
        ExprLexer lexer = new ExprLexer(CharStreams.fromStream(System.in));
        
        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Pass tokens to parser
        ExprParser parser = new ExprParser(tokens);
        
        // Make AST from prog and print the tree
        ExprParser.ProgContext ctx = parser.prog();
        ExprTree AST = (ExprTree)new BuildAstVisitor().visitProg(ctx);
        
        // Evaluate AST result
        Evaluate Evaluator = new Evaluate();
        ArrayList<Float> buffer = new ArrayList<Float>();
        AST.children.forEach(node -> buffer.add(Evaluator.evaluate(node)));
        for (Float val : buffer){
            if (val == null) return;
        }
        for (Float val : buffer){
            AST.children.forEach(node -> new AstCall().Call(node, 0));
            System.out.println(val);
        }
    }
}