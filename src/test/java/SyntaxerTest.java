import io.github.riicarus.common.ast.CodeFile;
import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.front.semantic.types.Scope;
import io.github.riicarus.front.syntax.Syntaxer;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Riicarus
 * @create 2024-1-25 17:39
 * @since 1.0.0
 */
public class SyntaxerTest {

    @Test
    public void testSyntaxer() {
        Syntaxer syntaxer = new Syntaxer();
        syntaxer.init("D:\\tmp\\compiler\\lex.txt", true);
        final CodeFile codeFile = syntaxer.parse();
        System.out.println(codeFile.toTreeString(0, "\t"));
        Queue<Scope> q = new LinkedList<>();
        q.add(codeFile.getScope());

        while (!q.isEmpty()) {
            Scope scope = q.poll();
            scope.getChildren().forEach(s -> {
                System.out.println(s);
                s.getElements().forEach((n, e) -> System.out.println("\t" + e));
            });
            q.addAll(scope.getChildren());
        }

        Scope s = codeFile.getScope();
        System.out.println();
        System.out.println(s.enter("Func#main#0"));
        System.out.println(s.enter("Func#foo#0"));

        System.out.println();
        for (Stmt stmt : codeFile.getStmts()) {
            if (stmt.getScope() != null) {
                System.out.println(s = s.enter(stmt.getScope().getName()));
                s = s.exit();
            }
        }
    }

}
