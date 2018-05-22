package suan.mydns.jp.music;

import suan.mydns.jp.Thoone;

public class MSTART
{
	public boolean start = false;
	private boolean starts = false;
	private long starttime = 0;
	private long time = 0;
	private int[] nowNotes = {0, 0, 0, 0};

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
			}

			boolean b = false;
			this.time = (long) (((System.currentTimeMillis() - this.starttime)/1000.0)*th.mm2.HzMu);
			for(int i = 0; i < 4; i++)
			{
				if(this.nowNotes[i] < th.SPT[i].Volume.size())
				{
					b = true;
					System.out.println(th.SPT[i].Time.get(this.nowNotes[i]) + " " + this.time);
					if(th.SPT[i].Time.get(this.nowNotes[i]) <= this.time)
					{
						th.mm2.ChStat(th.SPT[i].Freque.get(this.nowNotes[i]), th.SPT[i].Duty.get(this.nowNotes[i]), th.SPT[i].Volume.get(this.nowNotes[i]), th.SPT[i].Voldow.get(this.nowNotes[i]), th.SPT[i].Fredow.get(this.nowNotes[i]), true, this.nowNotes[i], i);
						if(this.time >= th.SPT[i].Time.get(this.nowNotes[i])+th.SPT[i].SoundT.get(this.nowNotes[i]))
						{
							this.nowNotes[i]++;
						}
					}
					else
					{
						th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i);
					}
				}
			}
			if(!b)
			{
				for(int i = 0; i < 4; i++)
				{
					th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i);
				}
				this.start = false;
			}
		}
		else if(this.starts)
		{
			this.starts = false;
		}
	}
}
