package suan.mydns.jp.io;

import java.io.BufferedInputStream;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import suan.mydns.jp.Thoone;
import suan.mydns.jp.music.MM2;

public class DPCMWaveInport
{
	Thoone th = null;
	String Path = "";
	public static boolean WaveLoadFinish = true;
	public static boolean LOADING = false;
	public static long finishtime = 0;
	private final double inportrate = 48000;//69514.98025587051;

	public void Load(Thoone th, String Path)
	{
		this.th = th;
		this.Path = Path;
		DPCMLOAD lo = new DPCMLOAD();
		lo.start();
	}

	class DPCMLOAD extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				LOADING = true;
				AudioInputStream input = AudioSystem.getAudioInputStream(new File(Path));
				AudioFormat format = input.getFormat();
				BufferedInputStream bufInput = new BufferedInputStream(input);
				byte[] frameBuf = new byte[format.getFrameSize()];

				int maxSize = (int) Math.pow(2, format.getSampleSizeInBits());
				double wa = 4; //maxSize / 128.0;
				double rate = format.getSampleRate();
				double changerate = rate / inportrate;
				//System.out.println(changerate);
				double inrate = 0;
				int waru = format.getChannels();//format.getSampleSizeInBits() / 8;

				//System.out.println(format.getSampleRate() + " " + format.getFrameRate() + " " + format.getSampleSizeInBits() + " " + maxSize + " " + wa + " " + format.getChannels() + " " + waru);
				int readed = 0;
				long loop = 0;
				long moreloop = 0;
				int times = 0;
				boolean breaks = false;
				int last = 0;

				MM2.DPCMo[th.SPT[4].freq].clear();

				while ((readed = bufInput.read(frameBuf)) > 0 && !breaks) {
					//System.out.println(readed + " " + loop);
					for(int i = 0; i < readed; i++)
					{
						if(i == 0 && loop == 0)
						{
							MM2.DPCMo[th.SPT[4].freq].add((byte)(frameBuf[i] / wa + 0.5));
							last = (byte)(frameBuf[i] / wa + 0.5);
						}
						else
						{
							try
							{
								//System.out.println(MM2.DPCMo[th.SPT[4].freq].get(times));
								while(inrate >= changerate)
								{
									if(last >= 32) MM2.DPCMo[th.SPT[4].freq].add((byte)0);
									else if(last <= -32) MM2.DPCMo[th.SPT[4].freq].add((byte)1);
									else if(last > (int)(frameBuf[i] / wa + 0.5)) MM2.DPCMo[th.SPT[4].freq].add((byte)0);
									else MM2.DPCMo[th.SPT[4].freq].add((byte)1);
									//System.out.println(MM2.DPCMo[th.SPT[4].freq].get(times + 1) + " " + inrate + " " + last + " " + (int)(frameBuf[i] / wa + 0.5) + " " + frameBuf[i] + " " + (last > (int)(frameBuf[i] / wa + 0.5)));
									//System.out.println(last + " " + (frameBuf[i] / wa + 0.5));
									last += MM2.DPCMo[th.SPT[4].freq].get(times + 1) * 2 - 1;
									if(last > 32)
									{
										System.out.println(times + " " + last + " " + (frameBuf[i] / wa + 0.5));
										last = 32;
									}
									if(last < -32)
									{
										System.out.println(times + " " + last + " " + (frameBuf[i] / wa + 0.5));
										last = -32;
									}
									inrate -= changerate * waru;
									times++;
								}
							}
							catch (IndexOutOfBoundsException e)
							{
								breaks = true;
								e.printStackTrace();
								break;
							}
						}
						if(moreloop % waru == 0)inrate++;
						moreloop++;
					}
					loop++;
				}//*/
				finishtime = System.currentTimeMillis();
				LOADING = false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
