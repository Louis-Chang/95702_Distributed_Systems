package ds.cmu.edu.task2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import ds.cmu.edu.task2.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private SharedViewModel sharedViewModel;
    private int player1Score = 0;
    private int player2Score = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Now you can access the shared variable, for example:
        String uuid = sharedViewModel.getUuid();
        Log.d("UUID", "UUID: " + uuid);

        // Retrieve player names from the arguments and set them
        if (getArguments() != null) {
            String playerOneName = getArguments().getString("playerOneName", "Player 1");
            String playerTwoName = getArguments().getString("playerTwoName", "Player 2");

            binding.PlayerOneName.setText(playerOneName); // Set the player one name to the TextView
            binding.PlayerTwoName.setText(playerTwoName); // Set the player two name to the TextView
        }

        binding.drawCardButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    JSONObject requestBodyJson = new JSONObject();
                    requestBodyJson.put("uuid", uuid); // Include the UUID in the request body

                    RequestBody requestBody = RequestBody.create(requestBodyJson.toString(), MediaType.parse("application/json"));

                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:8080/Task1-1.0-SNAPSHOT/card")
                            .post(requestBody) // Use a POST request
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray cards = jsonObject.getJSONArray("cards");
                            JSONObject card1 = cards.getJSONObject(0);
                            JSONObject card2 = cards.getJSONObject(1);

                            int player1CardValue = parseCardValue(card1.getString("value"));
                            int player2CardValue = parseCardValue(card2.getString("value"));
                            updateCardCode(card1.getString("value") + " " + card1.getString("suit"), card2.getString("value") + " " + card2.getString("suit"));
                            // Update UI elements on the main thread
                            getActivity().runOnUiThread(() -> {
                                if (player1CardValue > player2CardValue) {
                                    updateScore(1);
                                } else if (player1CardValue < player2CardValue) {
                                    updateScore(2);
                                } else {
                                    updateScore(0);
                                }
                                // Load the card images
                                try {
                                    Glide.with(SecondFragment.this)
                                            .load(card1.getString("image"))
                                            .into(binding.player1CardImage);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    Glide.with(SecondFragment.this)
                                            .load(card2.getString("image"))
                                            .into(binding.player2CardImage);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Failed to draw card", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int parseCardValue(String value) {
        switch (value) {
            case "ACE":
                return 14;
            case "KING":
                return 13;
            case "QUEEN":
                return 12;
            case "JACK":
                return 11;
            default:
                return Integer.parseInt(value); // For number cards
        }
    }

    private void updateCardCode(String player1CardCode, String player2CardCode) {
        getActivity().runOnUiThread(() -> {
            binding.player1CardCode.setText(player1CardCode);
            binding.player2CardCode.setText(player2CardCode);
        });
    }

    private void updateScore(int player) {
        String resultMessage;
        if (player == 1) {
            player1Score += 1; // Increment player 1's score
            resultMessage = "Player 1 wins this round!";
            getActivity().runOnUiThread(() ->
                    binding.player1Score.setText("Score: " + player1Score)
            );
        } else if (player == 2) {
            player2Score += 1; // Increment player 2's score
            resultMessage = "Player 2 wins this round!";
            getActivity().runOnUiThread(() ->
                    binding.player2Score.setText("Score: " + player2Score)
            );
        } else {
            resultMessage = "It's a tie!";
        }
        getActivity().runOnUiThread(() -> {
            binding.resultDisplay.setText(resultMessage); // Update the result display
        });

    }

}
