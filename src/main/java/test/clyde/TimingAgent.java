package test.clyde;
import java.lang.instrument.Instrumentation;

public class TimingAgent {
  public static void premain(String agentArgs, Instrumentation inst) {
    inst.addTransformer(new TimingTransformer(agentArgs));
  }
}