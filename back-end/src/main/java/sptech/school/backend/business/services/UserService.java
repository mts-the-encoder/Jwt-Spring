package sptech.school.backend.business.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import sptech.school.backend.business.services.abstractions.IUserService;
import sptech.school.backend.comunication.request.RegisterRequest;
import sptech.school.backend.comunication.response.UserResponse;
import sptech.school.backend.entities.User;
import sptech.school.backend.entities.enums.Role;
import sptech.school.backend.repositories.IUserRepository;

import javax.naming.NotContextException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final IUserRepository repository;
    private final AuthenticationManager authenticationManager;

    //Fazer a regra de negócio que traz todos
    @Override
    public List<UserResponse> findAll() throws NotContextException {
        var users = this.repository.findAllByRole(Role.ADMIN);

        if (users.isEmpty()) {
            throw new NotContextException("Not users are found");
        }

        return toResponseList(users);
    }

    @Override
    public Optional<UserResponse> findByFirstName(String firstName) {
        var user = this.repository.findByFirstName(firstName);

        var response = modelMapper.map(user, UserResponse.class);

        return Optional.of(response);
    }

    private UserResponse toResponse(User entity) {
        return modelMapper.map(entity, UserResponse.class);
    }

    private List<UserResponse> toResponseList(List<User> list) {
        return list.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse update(Integer id, RegisterRequest request) {
        var user = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        modelMapper.map(request, User.class);
        repository.save(user);

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public void deleteById(Integer id) {
        var user = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        repository.deleteById(user.getId());
    }
}
