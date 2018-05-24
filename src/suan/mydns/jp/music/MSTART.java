package suan.mydns.jp.music;

import java.util.Timer;
import java.util.TimerTask;

import suan.mydns.jp.Thoone;

public class MSTART
{
	public boolean start = false;
	private boolean starts = false;
	private long starttime = 0;
	private long time = 0;
	public int[] nowNotes = {0, 0, 0, 0};
	private Thoone th;

	public void Play(Thoone th)
	{
		if(this.start)
		{
			if(!this.starts)
			{
				this.starts = true;
				this.starttime = System.currentTimeMillis();
				for(int i = 0; i < 4; i++)
				{
					this.nowNotes[i] = 0;
				}
				this.th = th;

				Timer T2 = new Timer();
				T2.scheduleAtFixedRate(new AudioTask(), 0, 1000 / (th.mm2.HzMu / th.mm2.onecool));
			}
		}
		else if(this.starts)
		{
			this.starts = false;
		}
	}

	class AudioTask extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			if(start)
			{
				boolean b = false;
				time = (long) (((System.currentTimeMillis() - starttime)/1000.0)*th.mm2.HzMu);
				for(int i = 0; i < 4; i++)
				{
					if(nowNotes[i] < th.SPT[i].Volume.size())
					{
						b = true;
						if(th.SPT[i].Time.get(nowNotes[i]) <= time)
						{
							th.mm2.ChStat(th.SPT[i].Freque.get(nowNotes[i]), th.SPT[i].Duty.get(nowNotes[i]), th.SPT[i].Volume.get(nowNotes[i]), th.SPT[i].Voldow.get(nowNotes[i]), th.SPT[i].Fredow.get(nowNotes[i]), true, nowNotes[i], i);
							if(time >= th.SPT[i].Time.get(nowNotes[i])+th.SPT[i].SoundT.get(nowNotes[i]))
							{
								nowNotes[i]++;
							}
						}
						else
						{
							th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i);
						}
					}
					else
					{
						th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i);
					}
				}
				if(!b)
				{
					if(th.ch.loop)
					{
						starttime = System.currentTimeMillis();
						for(int i = 0; i < 4; i++)
						{
							nowNotes[i] = 0;
						}
					}
					else
					{
						for(int i = 0; i < 4; i++)
						{
							th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i);
						}
						start = false;
						this.cancel();
					}
				}
			}
		}
	}
}
