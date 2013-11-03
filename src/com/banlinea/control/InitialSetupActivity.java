package com.banlinea.control;

import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.banlinea.control.bussiness.AuthenticationService;
import com.banlinea.control.bussiness.BudgetService;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.entities.definitions.TimePeriod;
import com.banlinea.control.remote.util.CallResult;

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
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
		} else {
			finish();
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

			// Fragment fragment = new DummySectionFragment();
			// args = new Bundle();
			/*
			 * args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position +
			 * 1); fragment.setArguments(args);
			 */
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

	public static class IncomesSectionFragment extends Fragment {

		private ViewPager mViewPager;
		private EditText amountEditText;
		private Button nextButton;

		private final String SalaryCategoryId = "4ee36b0a-98d7-4aa6-9c3a-f2305ecd863a";

		public IncomesSectionFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_incomes,
					container, false);

			mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);

			amountEditText = (EditText) rootView
					.findViewById(R.id.amount_edittext);
			nextButton = (Button) rootView.findViewById(R.id.continue_button);
			nextButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					UserBudget ub = new UserBudget();

					try {
						ub.setIdCategory(SalaryCategoryId);
						ub.setIdUser(new AuthenticationService(v.getContext())
								.GetUser().getId());
						ub.setPeriod(TimePeriod.MONTHLY);
						ub.setBudget(Float.parseFloat(amountEditText.getText()
								.toString()));
						CallResult budgetResult = new BudgetService(v
								.getContext()).AddBudget(ub);
						if (budgetResult.isSuccessfullOperation())
							mViewPager.setCurrentItem(
									mViewPager.getCurrentItem() + 1, true);
						else
							Toast.makeText(getActivity(),
									budgetResult.getMessage(),
									Toast.LENGTH_SHORT).show();
					} catch (NumberFormatException e) {
						Toast.makeText(getActivity(),
								R.string.number_format_error_message,
								Toast.LENGTH_SHORT).show();
					}
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
		private EditText rentEditText;
		private long animationDuration = 1000;
		private Button continueButton;

		private final String ExpensesCategoryId = "7e590d6e-3614-11e3-98fd-ce3f5508acd9";

		public ExpensesSectionFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_expenses,
					container, false);

			mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);

			liveAloneLayout = (LinearLayout) rootView
					.findViewById(R.id.live_alone_question_layout);

			yesButton = (Button) rootView.findViewById(R.id.yes_button);
			yesButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					crossfade();
				}
			});

			noButton = (Button) rootView.findViewById(R.id.no_button);
			noButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1,
							true);
				}
			});

			rentLayout = (LinearLayout) rootView.findViewById(R.id.rent_layout);
			rentLayout.setVisibility(View.GONE);

			rentEditText = (EditText) rootView.findViewById(R.id.rent_edittext);

			continueButton = (Button) rootView
					.findViewById(R.id.continue_button);
			continueButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					UserBudget ub = new UserBudget();

					try {
						ub.setIdCategory(ExpensesCategoryId);
						ub.setIdUser(new AuthenticationService(v.getContext())
								.GetUser().getId());
						ub.setPeriod(TimePeriod.MONTHLY);
						ub.setBudget(Float.parseFloat(rentEditText.getText()
								.toString()));
					} catch (NumberFormatException e) {
						Toast.makeText(getActivity(),
								R.string.number_format_error_message,
								Toast.LENGTH_SHORT).show();
					}

					CallResult budgetResult = new BudgetService(v.getContext())
							.AddBudget(ub);
					if (budgetResult.isSuccessfullOperation())
						mViewPager.setCurrentItem(
								mViewPager.getCurrentItem() + 1, true);
					else
						Toast.makeText(getActivity(),
								budgetResult.getMessage(), Toast.LENGTH_SHORT)
								.show();
				}
			});

			return rootView;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
		private void crossfade() {

			liveAloneLayout.animate().alpha(0f).setDuration(animationDuration)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							liveAloneLayout.setVisibility(View.GONE);
							rentLayout.setAlpha(0f);
							rentLayout.setVisibility(View.VISIBLE);
							rentLayout.animate().alpha(1f)
									.setDuration(animationDuration)
									.setListener(null);
						}
					});

		}
	}

	public static class PublicServicesSectionFragment extends Fragment {

		private EditText energyEditText;
		private EditText waterEditText;
		private EditText gasEditText;
		private EditText otherEditText;
		private Button finishButton;

		private final String EnergyCategoryId = "7e591692-3614-11e3-98fd-ce3f5508acd9";
		private final String WaterCategoryId = "7e590f80-3614-11e3-98fd-ce3f5508acd9";
		private final String GasCategoryId = "7e5912c8-3614-11e3-98fd-ce3f5508acd9";
		private final String OtherCategoryId = "7e592506-3614-11e3-98fd-ce3f5508acd9";

		public PublicServicesSectionFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_public_services,
					container, false);

			energyEditText = (EditText) rootView
					.findViewById(R.id.energy_edittext);
			waterEditText = (EditText) rootView
					.findViewById(R.id.water_edittext);
			gasEditText = (EditText) rootView.findViewById(R.id.gas_edittext);
			otherEditText = (EditText) rootView
					.findViewById(R.id.others_edittext);

			finishButton = (Button) rootView.findViewById(R.id.finish_button);
			finishButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					UserBudget ubEnergy = new UserBudget();

					try {
						ubEnergy.setIdCategory(EnergyCategoryId);
						ubEnergy.setIdUser(new AuthenticationService(v
								.getContext()).GetUser().getId());
						ubEnergy.setPeriod(TimePeriod.MONTHLY);
						ubEnergy.setBudget(Float.parseFloat(energyEditText
								.getText().toString()));
					} catch (NumberFormatException e) {
						Toast.makeText(getActivity(),
								R.string.number_format_error_message,
								Toast.LENGTH_SHORT).show();
					}

					CallResult budgetResult = new BudgetService(v.getContext())
							.AddBudget(ubEnergy);
					if (budgetResult.isSuccessfullOperation()) {

						UserBudget ubWater = new UserBudget();

						try {
							ubWater.setIdCategory(WaterCategoryId);
							ubWater.setIdUser(new AuthenticationService(v
									.getContext()).GetUser().getId());
							ubWater.setPeriod(TimePeriod.MONTHLY);
							ubWater.setBudget(Float.parseFloat(waterEditText
									.getText().toString()));
						} catch (NumberFormatException e) {
							Toast.makeText(getActivity(),
									R.string.number_format_error_message,
									Toast.LENGTH_SHORT).show();
						}

						CallResult budgetResult1 = new BudgetService(v
								.getContext()).AddBudget(ubWater);
						if (budgetResult1.isSuccessfullOperation()) {

							UserBudget ubGas = new UserBudget();

							try {
								ubGas.setIdCategory(GasCategoryId);
								ubGas.setIdUser(new AuthenticationService(v
										.getContext()).GetUser().getId());
								ubGas.setPeriod(TimePeriod.MONTHLY);
								ubGas.setBudget(Float.parseFloat(gasEditText
										.getText().toString()));
							} catch (NumberFormatException e) {
								Toast.makeText(getActivity(),
										R.string.number_format_error_message,
										Toast.LENGTH_SHORT).show();
							}

							CallResult budgetResult2 = new BudgetService(v
									.getContext()).AddBudget(ubGas);
							if (budgetResult2.isSuccessfullOperation()) {

								UserBudget ubOther = new UserBudget();

								try {
									ubOther.setIdCategory(OtherCategoryId);
									ubOther.setIdUser(new AuthenticationService(
											v.getContext()).GetUser().getId());
									ubOther.setPeriod(TimePeriod.MONTHLY);
									ubOther.setBudget(Float
											.parseFloat(otherEditText.getText()
													.toString()));
								} catch (NumberFormatException e) {
									Toast.makeText(
											getActivity(),
											R.string.number_format_error_message,
											Toast.LENGTH_SHORT).show();
								}

								CallResult budgetResult3 = new BudgetService(v
										.getContext()).AddBudget(ubOther);
								if (budgetResult3.isSuccessfullOperation()) {

									getActivity().finish();

								} else {
									Toast.makeText(getActivity(),
											budgetResult3.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(getActivity(),
										budgetResult2.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getActivity(),
									budgetResult1.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(),
								budgetResult.getMessage(), Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			return rootView;
		}
	}
}
