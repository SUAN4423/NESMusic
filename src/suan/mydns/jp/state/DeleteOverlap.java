package suan.mydns.jp.state;

import suan.mydns.jp.Thoone;

public class DeleteOverlap
{
	private boolean pressed = false;
	private long time = 0;
	private Thoone th;

	public void Draw(Thoone th)
	{
		th.fill(0xFF);
		if(th.kmState.IsMouseIn(1280/2+1280/12, 0, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.pressed)
			{
				this.pressed = true;
				this.th = th;
				Overlap ol = new Overlap();
				ol.start();
			}
			else if(!th.kmState.MLeft && this.pressed)
			{
				this.pressed = false;
			}
		}
		th.rect(1280/2+1280/12, 0, 1280/12, 720/8);
		th.fill(0x00);
		th.textSize(20);
		if(System.currentTimeMillis()-this.time < 1000)
		{
			th.text("Delete\nOverlap\nFinished", 1280/2+1280/12+10, 25);
		}
		else
		{
			th.text("Delete\nOverlap", 1280/2+1280/12+10, 25);
		}
	}

	class Overlap extends Thread
	{
		@Override
		public void run()
		{
			for(int i = 0; i < 5; i++)
			{
				for(int j = 0; j < th.SPT[i].Time.size()-1; j++)
				{
					if(th.SPT[i].Time.get(j).equals(th.SPT[i].Time.get(j+1)))
					{
						th.SPT[i].Volume.remove(j+1);
						th.SPT[i].Freque.remove(j+1);
						th.SPT[i].FrequI.remove(j+1);
						th.SPT[i].Time.remove(j+1);
						th.SPT[i].SoundT.remove(j+1);
						th.SPT[i].Duty.remove(j+1);
						th.SPT[i].Voldow.remove(j+1);
						th.SPT[i].Fredow.remove(j+1);
						th.SPT[i].VolDUM.remove(j+1);
						th.SPT[i].Moduration.remove(j+1);
						j--;
					}
				}
			}
			time = System.currentTimeMillis();
		}
	}
}
