import io.github.riicarus.front.lex.Lexer;
import org.junit.Test;

/**
 * Test for Lexer
 *
 * @author Riicarus
 * @create 2024-1-13 12:02
 * @since 1.0.0
 */
public class LexerTest {

    @Test
    public void testLexer() {
        Lexer lexer = new Lexer();
        lexer.init("D:\\tmp\\compiler\\lex.txt", true);
        try {
            lexer.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
