import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Jien217 on 2015/12/16.
 *
 * @author Jien217
 */
public class ProcessResult {
    static ScriptEngineManager manager = new ScriptEngineManager();
    static ScriptEngine engine = manager.getEngineByName("js");
    static StringBuilder builder = new StringBuilder("");
    static StringBuilder temp = new StringBuilder("");
    static Object result = new Object();

    public static String processResult(String equationText) {
        builder.append(equationText);
        temp.append(builder.toString().replace("×", "*"));
        builder.delete(0, builder.length());
        builder.append(temp.toString().replace("÷", "/"));

        try {
            result = engine.eval(builder.toString());
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        builder.delete(0, builder.length());
        temp.delete(0, temp.length());

        return result.toString();
    }
}
