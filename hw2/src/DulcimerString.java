import java.util.*;

/**
 * A model of a string on a dulcimer for a particular note
 *
 * @author Eli Blaney
 * @version 1.0
 */
public class DulcimerString {

	private final static List<String> NOTES = List.of("C,C#,D,D#,E,F,F#,G,G#,A,A#,B".split(","));
	private final static Random r = new Random();

	private final String note;
	private final Queue<Double> soundData;

	/**
	 * Creates an instance of a dulcimer string model for a particular note.
	 *
	 * @param note The string representation of the note modelled by this string,
	 *             where +/- displaces octave. E.g. "C", "A#", "D#+".
	 */
	public DulcimerString(String note) {
		this.note = note;
		int offset = getOffsetFromMiddleC();
		/*
		 * The sample length models the note frequency based on its offset, sample rate, and tuning at A4 = 440 Hz
		 * Interestingly, this transposes downwards by 4 half steps, so "C" really sounds the frequency of "G#-"
		 * This problem is fixed in the DulcimerString#getOffsetFromMiddleC method by transposing up 4 half steps
		 */
		int sampleLength = (int) Math.round(
			StdAudio.SAMPLE_RATE * Math.pow(2, (22.0- offset)/12) / 440
		);
		this.soundData = new LinkedList<>(Collections.nCopies(sampleLength, 0.0));
	}

	/**
	 * Samples the string by polling the first sound double in the queue and adding a decayed sound double to the end
	 * of the queue.
	 *
	 * @return The sound double at the front of the queue.
	 */
	public double sample() {
		Double sample = soundData.poll();
		Double next = soundData.peek();
		if(sample == null || next == null) {
			// Queue is empty for some reason, abort to avoid NPE.
			System.err.println("Warning: Dulcimer string queue is empty!");
			return 0.0;
		}
		// Volume decay
		soundData.add(0.996 / 2 * (sample + next));
		return sample;
	}

	/**
	 * Strike the dulcimer string, filling the queue with random data.
	 */
	public void strike() {
		for(int i = 0; i < soundData.size(); i++) {
			soundData.poll();
			soundData.add(r.nextDouble() - 0.5);
		}
	}

	/**
	 * Get the numerical offset of the note from middle C, represented as an integer number of half steps
	 *
	 * @return The number of half steps above (or below) middle C
	 */
	public int getOffsetFromMiddleC() {
		String note = this.getNote();
		// Identify the note in the chromatic scale, where C is 0 and B is 11
		int offset = NOTES.indexOf(note.replace("+", "").replace("-", "")) - 3;
		// Octave displacement for each + or -
		while(note.contains("+")) {
			note = note.replaceFirst("\\+", "");
			offset += 12;
		}
		while(note.contains("-")) {
			note = note.replaceFirst("-", "");
			offset -= 12;
		}
		/*
		 * Transpose up 4 half steps, to account for the fact that the sample
		 * length equation transposes down 4 half steps for some reason.
		 */
		return offset + 4;
	}

	/**
	 * Get the note modelled by this dulcimer string
	 *
	 * @return The String representation of the note modelled by this string, e.g. "F#-".
	 */
	public String getNote() {
		return this.note;
	}
}
