package suan.mydns.jp.state;

import suan.mydns.jp.Thoone;
import suan.mydns.jp.track.SuperTrack;

public class Move
{
	public boolean enableMove = false;
	private boolean pressed = false;
	private boolean first = false;
	private int[] position = {0, 0};
	private long[] x = {0, 0};

	public void move(Thoone th, SuperTrack st)
	{
		int a = (th.kmState.Mouse[0]-1280/6-60-st.ShiftX);
		int k = -st.ShiftX/(1280/6)*st.Nag;
		while(true)
		{
			if(k*(1280/6)/st.Nag <= a && (k+1)*(1280/6)/st.Nag > a)
			{
				break;
			}
			k++;
		}
		long b = (long) ((k)*((60.0 / st.Tempo) * th.mm2.HzMu * 4 / st.Nag));


		if(th.kmState.IsMouseIn((int)(x[0]/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + st.ShiftX), 720/4, (int)((x[1] - x[0])/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6)), 720/4*3))
		{

		}
		else
		{
			if(th.kmState.MLeft)
			{
				if(!this.first)
				{
					this.first = true;
					this.position[0] = th.kmState.Mouse[0];
					this.position[1] = th.kmState.Mouse[1];
					x[0] = b;
				}
				x[1] = b;
			}
			else
			{
				this.first = false;
			}
		}

		th.fill(0xFF, 0x00, 0x00, 0x90);
		th.rect(x[0]/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + st.ShiftX, 720/4, (x[1] - x[0])/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6), 720/4*3);
	}

	public void Draw(Thoone th)
	{
		th.fill(0xFF);
		if(th.kmState.IsMouseIn((int)(1280/6*4.5f), 0, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(!this.pressed && th.kmState.MLeft)
			{
				this.pressed = true;
				this.enableMove = !this.enableMove;
			}
			else if(this.pressed && !th.kmState.MLeft)
			{
				this.pressed = false;
			}
		}
		else if(this.enableMove)
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		th.rect(1280/6*4.5f, 0, 1280/12, 720/8);
		th.fill(0);
		th.textSize(20);
		th.text("Select\nmode", 1280/6*4.5f+10, 25);
	}
}
