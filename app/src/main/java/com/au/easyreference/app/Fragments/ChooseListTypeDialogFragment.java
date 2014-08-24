package com.au.easyreference.app.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceList;

import static android.view.View.OnClickListener;

/**
 * Created by Marcus on 31/05/2014.
 */
public class ChooseListTypeDialogFragment extends DialogFragment
{
	@InjectView(R.id.new_apa_reference_list)
	protected TextView apa;
	@InjectView(R.id.new_harvard_reference_list)
	protected TextView harvard;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);

		View layout = getActivity().getLayoutInflater().inflate(R.layout.choose_reference_type_dialog_fragment, null);
		ButterKnife.inject(this, layout);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		apa.setOnClickListener(new SelectTypeListener());
		harvard.setOnClickListener(new SelectTypeListener());

		return layout;
	}

	public class SelectTypeListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ReferenceListDialogFragment dialog = new ReferenceListDialogFragment();
			Bundle args = new Bundle();

			if(v.getId() == apa.getId())
				args.putInt(ReferenceListDialogFragment.KEY_TYPE, ReferenceList.APA);
			else if(v.getId() == harvard.getId())
				args.putInt(ReferenceListDialogFragment.KEY_TYPE, ReferenceList.HARVARD);

			dialog.setArguments(args);
			android.app.FragmentManager manager = getFragmentManager();
			dialog.show(manager, "Reference Dialog");
			dismiss();
		}
	}
}
