
package prog2.vista;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Daniel Ortiz
 */
public class VariableNormal implements Serializable {
    private Random random;
    private float mean;
    private float standardDeviation;

    public VariableNormal(float mean, float standardDeviation, long seed) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.random = new Random(seed);
    }

    public float seguentValor() {
        return (float) mean + standardDeviation * (float) random.nextGaussian();
    }

}
