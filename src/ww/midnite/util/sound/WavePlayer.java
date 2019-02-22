package ww.midnite.util.sound;

import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;


public class WavePlayer implements LineListener {

	private final URL waveFileURL;
	private final ThreadPoolExecutor threadPool;
	private Clip clip;


	public WavePlayer(final URL waveFile) {
		this(waveFile, null);
	}


	public WavePlayer(final URL waveFileURL, final ThreadPoolExecutor threadPool) {
		this.waveFileURL = waveFileURL;
		this.threadPool = threadPool;
	}


	public void play() {
		if (threadPool != null) {
			threadPool.execute(playback());
		} else {
			new Thread(playback()).start();
		}
	}


	private Runnable playback() {
		return new Runnable() {

			@Override
			public void run() {
				playFile();
			}
		};
	}


	private synchronized void playFile() {
		try {
			clip = AudioSystem.getClip();
			clip.addLineListener(this);

			clip.open(AudioSystem.getAudioInputStream(waveFileURL));
			clip.start();

		} catch (final Exception e) {
			e.printStackTrace();
			close();
		}
	}


	private synchronized void close() {
		clip.drain();
		clip.close();
	}


	@Override
	public void update(final LineEvent event) {
		if (event.getType().equals(LineEvent.Type.STOP)) {
			close();
		}
	}

}
