// Copyright (c) 2025 Douglas Miller <durgadas311@gmail.com>

import java.util.Properties;
import java.io.*;
import java.awt.event.*;
import javax.sound.sampled.*;

class Beep implements ActionListener {
	Clip beep;
	private javax.swing.Timer timer;

	public Beep(Properties props, String pfx) {
		timer = new javax.swing.Timer(250, this);
		String s = props.getProperty(pfx + "_beep");
		if (s == null) {
			s = pfx + "_beep.wav";
		} else if (s.length() == 0) {
			beep = null;
			return;
		}
		String beep_wav = s;
		try {
			InputStream is = SimResource.open(this, beep_wav);
			AudioInputStream wav =
				AudioSystem.getAudioInputStream(
					new BufferedInputStream(is));
			AudioFormat format = wav.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			beep = (Clip)AudioSystem.getLine(info);
			beep.open(wav);
			//beep.setLoopPoints(0, loop);
		} catch (Exception ee) {
			ee.printStackTrace();
			beep = null;
			return;
		}
		int volume = 50;
		s = props.getProperty(pfx + "_beep_volume");
		if (s != null) {
			volume = Integer.valueOf(s);
			if (volume < 0) volume = 0;
			if (volume > 100) volume = 100;
		}
		FloatControl vol = null;
		if (beep.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			vol = (FloatControl)beep.getControl(FloatControl.Type.MASTER_GAIN);
		} else if (beep.isControlSupported(FloatControl.Type.VOLUME)) {
			vol = (FloatControl)beep.getControl(FloatControl.Type.VOLUME);
		}
		if (vol != null) {
			float min = vol.getMinimum();
			float max = vol.getMaximum();
			float gain = (float)(min + ((max - min) * (volume / 100.0)));
			vol.setValue(gain);
		} else {
			System.err.format(pfx + ":Beep: no volume control\n");
		}
	}

	// TODO: race condition: ding() and actionPerformed()
	// race such that ding() restarts timer and audio but then
	// actionPerformed() cancels it.
	public synchronized void ding() {
		timer.removeActionListener(this);
		timer.addActionListener(this);
		timer.restart();
		if (!beep.isActive()) {
			beep.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	public synchronized void cancel() {
		timer.removeActionListener(this);
		beep.stop();
		beep.flush();
		beep.setFramePosition(0);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() != timer) {
			return;
		}
		cancel();
	}
}
