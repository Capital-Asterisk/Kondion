package vendalenger.kondion.lwjgl.resource;

import java.nio.ByteBuffer;

import javax.script.ScriptException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import com.sun.org.apache.xpath.internal.operations.Number;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.Window;

public class KAudio {

	private ScriptObjectMirror oneline;
	private int bufferId;
	private String source;
	private boolean isLoaded;

	public KAudio(String source) {
		this.source = source;
	}

	public void load() {
		// ol:8000:16000:10:t<<4*3dfwhatever
		if (source.startsWith("ol:")) {
			// itsa one liner
			String[] split = source.split(":");
			int frequency = Integer.parseInt(split[1]);
			int bufferFrequency = Integer.parseInt(split[2]);
			double duration = Float.parseFloat(split[3]);
			try {
				oneline = (ScriptObjectMirror) Kondion.getNashorn().eval("function(t) {return " + 
						source.substring(split[1].length() + split[2].length() + split[3].length() + 6, source.length()) + "}");
				bufferId = AL10.alGenBuffers();
				//int size = (int) (duration * bufferFrequency + 1);
				int size = (int) (duration * bufferFrequency);
				ByteBuffer buffy = BufferUtils.createByteBuffer(size);
				//double remainder = 0;
				//double add = (((double) bufferFrequency) / ((double) frequency)) % 1;
				for (int t = 0; t < size; t++) {
					
					Integer p = ((Double) oneline.call(null, t * ((double) frequency / (double) bufferFrequency))).intValue();
					buffy.put(p.byteValue());
					//System.out.println(p);
					
					//for (int i = -1; i < (bufferFrequency / frequency); i++) {
					//	buffy.put(p.byteValue());
					//}
					
					//remainder += add;
					
					//if (add != 0 && remainder % 1 == 0) {
					//	remainder = 0;
					//	buffy.put(p.byteValue());
					//	System.out.println("add: " + add);
					//}
					
					//buffy.put((byte) ((Math.random() * 255 * ((double) (t - size) / size)) - 127));
					
					//buffy.put((byte)(((i / 6) % 2) * Byte.MAX_VALUE - 127));
					//buffy.put((byte) ((t / 2 * (((t >> 4 | t) >> 8) % 11) & 255) / 4 + (t * (((t >> 5 | t) >> 8) % 11) & 100) / 2));
					//new Random().
				}
				//byte[] f = new byte[1000];
				//for (int i = 0; i < f.length; i++) {
				//	f[i] *= i / 1000;
				//}
				//new Random().nextBytes(f);
				//buffy.put(f);
				buffy.flip();
				AL10.alBufferData(bufferId, AL10.AL_FORMAT_MONO8, buffy, bufferFrequency);
				isLoaded = true;
				//int src = Window.src;
				//AL10.alSourcei(src, AL10.AL_BUFFER, bufferId);
				//AL10.alSourcef(src, AL10.AL_PITCH, 1.0f);
				//AL10.alSourcef(src, AL10.AL_GAIN, 1.0f);
				//AL10.alSource3f(src, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
				//AL10.alSource3f(src, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
				//AL10.alSourcePlay(src);
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getId() {
		return bufferId;
	}
	
	public void unLoad() {
		
	}
	
}
