package com.banlinea.control;

import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Daniel
 *
 */

public class InitialSetupActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial_setup);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public void onBackPressed() {
		if (mViewPager.getCurrentItem() > 0) {
			mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1, true);
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.initial_setup, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new IncomesSectionFragment();
				Log.d(InitialSetupActivity.class.getName(), "case 0");
				break;
			case 1:
				fragment = new ExpensesSectionFragment();
				Log.d(InitialSetupActivity.class.getName(), "case 1");
				break;
			case 2:
				fragment = new PublicServicesSectionFragment();
				Log.d(InitialSetupActivity.class.getName(), "case 2");
				break;

			default:
				Log.d(InitialSetupActivity.class.getName(), "case n");
				break;
			}
			
			//Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			/*args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);*/
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_initial_setup_dummy, container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	
	
	public static class IncomesSectionFragment extends Fragment {
		
		private ViewPager mViewPager;
		private EditText amountEditText;
		private Button nextButton;
		
		public IncomesSectionFragment() {
			
		}

		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_incomes, container, false);
			
			mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
			
			amountEditText = (EditText) rootView.findViewById(R.id.amount_edittext);
			nextButton = (Button) rootView.findViewById(R.id.continue_button);
			nextButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
				}
			});
			
			return rootView;
		}	
	}
	
	public static class ExpensesSectionFragment extends Fragment {
		
		private ViewPager mViewPager;
		private LinearLayout liveAloneLayout;
		private Button yesButton;
		private Button noButton;
		private LinearLayout rentLayout;
		private long animationDuration = 1000;
		private Button continueButton;
		
		public ExpensesSectionFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_expenses, container, false);
			
			mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
			
			liveAloneLayout = (LinearLayout) rootView.findViewById(R.id.live_alone_question_layout);
			
			yesButton = (Button) rootView.findViewById(R.id.yes_button);
			yesButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					crossfade();
				}
			});
			
			
			noButton = (Button) rootView.findViewById(R.id.no_button);
			noButton.setOnClickListener(
					new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
				}
			});
			
			rentLayout = (LinearLayout) rootView.findViewById(R.id.rent_layout);
			rentLayout.setVisibility(View.GONE);
			
			continueButton = (Button) rootView.findViewById(R.id.continue_button);
			continueButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
				}
			});
			
			return rootView;
		}
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
		private void crossfade() {
			
			liveAloneLayout.animate().alpha(0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					liveAloneLayout.setVisibility(View.GONE);
					rentLayout.setAlpha(0f);
					rentLayout.setVisibility(View.VISIBLE);
					rentLayout.animate().alpha(1f).setDuration(animationDuration).setListener(null);
				}
			});
			
			
		}
	}

	public static class PublicServicesSectionFragment extends Fragment {
		
		private ViewPager mViewPager;
		private Button finishButton;
		
		public PublicServicesSectionFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_public_services, container, false);
			
			mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
			
			finishButton = (Button) rootView.findViewById(R.id.finish_button);
			finishButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =  new Intent(getActivity(), BalanceActivity.class);
					startActivity(intent);
					getActivity().finish();
				}
			});
			return rootView;
		}
	}
}
