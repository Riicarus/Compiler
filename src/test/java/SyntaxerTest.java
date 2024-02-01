import io.github.riicarus.common.ast.CodeFile;
import io.github.riicarus.front.syntax.Syntaxer;
import org.junit.Test;

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
        final CodeFile codeFile = syntaxer.codeFile();
        System.out.println(codeFile.toTreeString(0, "\t"));
    }

}
