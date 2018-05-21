package suan.mydns.jp;

import processing.core.PApplet;
import suan.mydns.jp.channel.Channel;
import suan.mydns.jp.music.MM2;
import suan.mydns.jp.state.KMState;
import suan.mydns.jp.track.FourCh;
import suan.mydns.jp.track.OneCh;
import suan.mydns.jp.track.SuperTrack;
import suan.mydns.jp.track.ThreeCh;
import suan.mydns.jp.track.TwoCh;

public class Thoone extends PApplet
{
	public MM2 mm2 = new MM2();
	public KMState kmState = new KMState(this);
	public Channel ch = new Channel();
	public SuperTrack[] SPT = new SuperTrack[4];

	public static void main(String[] args)
	{
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		PApplet.main("suan.mydns.jp.Thoone");
	}

	@Override
	public void settings()
	{
		size(1280, 720);
	}

	@Override
	public void setup()
	{
		SPT[0] = new OneCh();
		SPT[1] = new TwoCh();
		SPT[2] = new ThreeCh();
		SPT[3] = new FourCh();
	}

	@Override
	public void draw()
	{
		fill(255);
		rect(0, 0, 1280, 720);
		SPT[ch.GetChannel()].Draw(this);
		ch.Draw(this);
		kmState.Mouse[0] = this.mouseX;
		kmState.Mouse[1] = this.mouseY;
	}

	@Override
	public void mousePressed()
	{
		if( mouseButton == LEFT )
		{
			//���{�^���������ꂽ�Ƃ��̏���
			kmState.MLeft = true;
		}
		else if( mouseButton == CENTER )
		{
			//�����{�^���������ꂽ�Ƃ��̏���
			kmState.MCenter = true;
		}
		else if( mouseButton == RIGHT )
		{
			//�E�{�^���������ꂽ�Ƃ��̏���
			kmState.MRight = true;
		}
	}

	@Override
	public void mouseReleased()
	{
		if( mouseButton == LEFT )
		{
			//���{�^���������ꂽ�Ƃ��̏���
			kmState.MLeft = false;
		}
		else if( mouseButton == CENTER )
		{
			//�����{�^���������ꂽ�Ƃ��̏���
			kmState.MCenter = false;
		}
		else if( mouseButton == RIGHT )
		{
			//�E�{�^���������ꂽ�Ƃ��̏���
			kmState.MRight = false;
		}
	}


	@Override
	public void keyPressed()
	{
		kmState.Key.put(keyCode, true);
		kmState.KeyC.put(key, true);
	}

	@Override
	public void keyReleased()
	{
		kmState.Key.put(keyCode, false);
		kmState.KeyC.put(key, false);
	}
}
