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
import java.io.IOException;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ds.cmu.edu.task2.databinding.FragmentFirstBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Now you can access the shared variable, for example:
        String uuid = sharedViewModel.getUuid();
        Log.d("UUID", "UUID: " + uuid);

        binding.startGameButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                String urlWithUUID = "http://10.0.2.2:8080/Task1-1.0-SNAPSHOT/card?uuid=" + uuid;
                Request request = new Request.Builder()
                        .url(urlWithUUID)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Bundle bundle = new Bundle();
                        bundle.putString("playerOneName", binding.playerOneName.getText().toString());
                        bundle.putString("playerTwoName", binding.playerTwoName.getText().toString());

                        getActivity().runOnUiThread(() ->
                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
                        );
                    } else {
                        // Handle API error on UI thread
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "Failed to start", Toast.LENGTH_LONG).show()
                        );
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Failed to start", Toast.LENGTH_LONG).show()
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
}
