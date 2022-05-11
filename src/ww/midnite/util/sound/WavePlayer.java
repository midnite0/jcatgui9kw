package ww.midnite.util.sound;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.ThreadPoolExecutor;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;


public class WavePlayer implements LineListener {

	private final byte[] waveFileData;
	private final ThreadPoolExecutor threadPool;
	private Clip clip;


	public WavePlayer(final URL waveFile) {
		this(waveFile, null);
	}


	public WavePlayer(final URL waveFileURL, final ThreadPoolExecutor threadPool) {
		waveFileData = read(waveFileURL);
		this.threadPool = threadPool;
	}


	private byte[] read(final URL waveFileURL) {
		try {
			return Files.readAllBytes(new File(waveFileURL.toURI()).toPath());
		} catch (final Exception e) {
			return new byte[0];
		}
	}


	public synchronized void play() {
		final Runnable playback = new Runnable() {
			@Override
			public void run() {
				playFile();
			}
		};

		if (threadPool != null) {
			threadPool.execute(playback);
		} else {
			new Thread(playback).start();
		}
	}


	public synchronized void stop() {
		final Runnable stop = new Runnable() {
			@Override
			public void run() {
				clip.drain();
				clip.close();
			}
		};
		if (threadPool != null) {
			threadPool.execute(stop);
		} else {
			new Thread(stop).start();
		}
	}


	private synchronized void playFile() {
		try {
			clip = AudioSystem.getClip();
			clip.addLineListener(this);

			clip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(waveFileData)));
			clip.start();

		} catch (final Exception e) {
			e.printStackTrace();
			stop();
		}
	}


	@Override
	public void update(final LineEvent event) {
		if (event.getType().equals(LineEvent.Type.STOP)) {
			stop();
		}
	}

}
