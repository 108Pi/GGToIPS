import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GGToIPS {
	private static char[] letters = { 'A', 'P', 'Z', 'L', 'G', 'I', 'T', 'Y', 'E', 'O', 'X', 'U', 'K', 'S', 'V', 'N' };

	public static void main(String[] args) throws IOException {
		if (args.length==0) {
			System.out.println("no args");
			return;
		}
		String ggcode = args[0];
		ggcode = ggcode.toUpperCase();
		
		int bitstring = 0;
		for (int i = 0; i < 6; i++) {
			bitstring <<= 4;
			bitstring |= toHex(ggcode.charAt(i));
		}
		
		int value;
		int address;
		int temp;
		
		bitstring <<= 8;
		value = ((bitstring >> 28) & 0x8) | ((bitstring >> 24) & 0x7);
		temp = (bitstring & 0x800) >> 8;
		temp |= ((bitstring >> 28) & 0x7);
		value <<= 4;
		value |= temp;
		address = (bitstring & 0x70000) >> 16;
		temp = ((bitstring & 0x8000) >> 12) | ((bitstring & 0x700) >> 8);
		address <<= 4;
		address |= temp;
		temp = ((bitstring & 0x8000000) >> 24) | ((bitstring & 0x700000) >> 20);
		address <<= 4;
		address |= temp;
		temp = ((bitstring & 0x80000) >> 16) | ((bitstring & 0x7000) >> 12);
		address <<= 4;
		address |= temp;
		temp = ((bitstring & 0x800) >> 8) | ((bitstring & 0x70) >> 4);
		
		address += 16;
		byte[] addressBytes = ByteBuffer.allocate(4).putInt(address).array();
		byte[] ips = {'P','A','T','C','H',0,127,127,0,1,127,'E','O','F'};
		ips[6] = addressBytes[2]; 
		ips[7] = addressBytes[3];
		ips[10] = (byte)value;
		
		File outputFile = new File(ggcode + ".ips");
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		outputStream.write(ips);
		outputStream.close();
	}

	public static int toHex(char letter) {
		letter = Character.toUpperCase(letter);
		for (int i = 0; i < letters.length; i++) {
			if (letters[i] == letter) {
				return i;
			}
		}
		return 0;
	}
}
